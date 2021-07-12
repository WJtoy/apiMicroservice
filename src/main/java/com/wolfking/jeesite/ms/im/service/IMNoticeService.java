package com.wolfking.jeesite.ms.im.service;

import com.google.common.collect.Lists;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.common.MSPage;
import com.kkl.kklplus.entity.sys.IMNoticeInfo;
import com.kkl.kklplus.entity.sys.IMNoticeUser;
import com.kkl.kklplus.entity.sys.mq.MQIMMessage;
import com.kkl.kklplus.entity.sys.vm.IMNoticeInfoSearchVM;
import com.wolfking.jeesite.common.persistence.Page;
import com.kkl.kklplus.utils.SequenceIdUtils;
import com.wolfking.jeesite.ms.im.entity.IMNoticeModel;
import com.wolfking.jeesite.ms.im.entity.mapper.IMNoticeMapper;
import com.wolfking.jeesite.ms.im.feign.IMFeign;
import com.wolfking.jeesite.ms.im.mq.sender.IMMessageMQSender;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


/**
 * 站内即时消息服务
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class IMNoticeService {

    @Autowired
    private IMFeign imFeign;

    @Autowired
    private IMMessageMQSender imMessageMQSender;

    //region 管理

    /**
     * 分页查询
     * @param page,b2BServiceTypeMapping
     * @return
     */
    public Page<IMNoticeModel> getList(Page<IMNoticeModel> page, IMNoticeInfoSearchVM searchVM){
        Page<IMNoticeModel> imNoticeInfoPage = new Page<>();
        imNoticeInfoPage.setPageSize(page.getPageSize());
        imNoticeInfoPage.setPageNo(page.getPageNo());
        searchVM.setPage(new MSPage<>(imNoticeInfoPage.getPageNo(), imNoticeInfoPage.getPageSize()));
        try {
            MSResponse<MSPage<IMNoticeInfo>> returnSystemNotice = imFeign.getNoticeList(searchVM);
            if (MSResponse.isSuccess(returnSystemNotice)) {
                MSPage<IMNoticeInfo> data = returnSystemNotice.getData();
                imNoticeInfoPage.setCount(data.getRowCount());
                if(!ObjectUtils.isEmpty(data.getList())){
                    List<IMNoticeModel> list = Lists.newArrayList();
                    IMNoticeModel view;
                    for(IMNoticeInfo noticeInfo:data.getList()){
                        view = Mappers.getMapper(IMNoticeMapper.class).beanToViewModel(noticeInfo);
                        list.add(view);
                    }
                    imNoticeInfoPage.setList(list);
                }
                //imNoticeInfoPage.setList(data.getList());
            } else {
                imNoticeInfoPage.setCount(0);
                imNoticeInfoPage.setList(Lists.newArrayList());
            }
        } catch (Exception e){
            imNoticeInfoPage.setMessage(MSErrorCode.FALLBACK_FAILURE.msg);
        }
        return imNoticeInfoPage;
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public IMNoticeInfo getNoticeById(Long id){
        MSResponse<IMNoticeInfo> mSResponse = imFeign.getNoticeById(id);
        if(MSResponse.isSuccess(mSResponse)){
            return mSResponse.getData();
        }else{
            return null;
        }
    }


    /**
     * 保存
     */
    @Transactional()
    public void saveNewNotice(IMNoticeInfo notice){
        notice.setNoticeType(MQIMMessage.NoticeType.Notice_VALUE);
        notice.setCreateAt(System.currentTimeMillis());
        MSResponse<IMNoticeInfo> msResponse = imFeign.newNotice(notice);
        if(MSResponse.isSuccess(msResponse)){
            //去掉html标签以及空格
            //String content = StringUtils.replaceHtml(Encodes.unescapeHtml(notice.getContent()));
            //content = content.replaceAll("&nbsp;", "").trim();
            //1.im 消息队列
            MQIMMessage.IMMessage message  = MQIMMessage.IMMessage.newBuilder()
                    .setId(notice.getId())
                    .setNoticeType(MQIMMessage.NoticeType.Notice)
                    .setUserTypes(notice.getUserTypes())
                    .setTitle(notice.getTitle())
                    .setSubTitle(notice.getSubTitle())
                    .setContent(notice.getContent())
                    .setCreateBy(notice.getCreateById())
                    .setCreateName(notice.getCreateBy().toString())
                    .setCreateDt(notice.getCreateAt())
                    .build();
            imMessageMQSender.send(message);
        }else{
            throw new RuntimeException(msResponse.getMsg());
        }
    }

    /**
     * 重送
     * @param notice
     */
    public void resend(IMNoticeInfo notice){
        //1.im 消息队列
        MQIMMessage.IMMessage message  = MQIMMessage.IMMessage.newBuilder()
                .setIsResend(1)//*
                .setId(notice.getId())
                .setNoticeType(MQIMMessage.NoticeType.Notice)
                .setUserTypes(notice.getUserTypes())
                .setTitle(notice.getTitle())
                .setSubTitle(notice.getSubTitle())
                .setContent(notice.getContent())
                .setCreateBy(notice.getCreateById())
                .setCreateName(notice.getCreateBy().toString())
                .setCreateDt(notice.getCreateAt())
                .build();
        imMessageMQSender.send(message);
    }

    public MSResponse<Boolean> cancel(long id){
        return imFeign.cancel(id);
    }

    //endregion

    //region 网点

    // 网点因帐号太多，查询公告信息表，不记录阅读时间

    /**
     * 分页查询网点的公告
     */
    public Page<IMNoticeInfo> getListForServicePoint(Page<IMNoticeInfo> page, IMNoticeInfoSearchVM searchVM){
        Page<IMNoticeInfo> imNoticeInfoPage = new Page<>();
        imNoticeInfoPage.setPageSize(page.getPageSize());
        imNoticeInfoPage.setPageNo(page.getPageNo());
        searchVM.setPage(new MSPage<>(imNoticeInfoPage.getPageNo(), imNoticeInfoPage.getPageSize()));
        searchVM.setUserTypes(1<<IMNoticeInfo.UserType.SERVICEPOINT.getCode());//网点
        searchVM.setIsCanceled(0); //未撤销
        try {
            MSResponse<MSPage<IMNoticeInfo>> returnSystemNotice = imFeign.getNoticeList(searchVM);
            if (MSResponse.isSuccess(returnSystemNotice)) {
                MSPage<IMNoticeInfo> data = returnSystemNotice.getData();
                imNoticeInfoPage.setPageNo(returnSystemNotice.getData().getPageNo());
                imNoticeInfoPage.setCount(data.getRowCount());
                imNoticeInfoPage.setList(data.getList());
            } else {
                imNoticeInfoPage.setCount(0);
                imNoticeInfoPage.setList(Lists.newArrayList());
            }
        } catch (Exception e){
            imNoticeInfoPage.setMessage(MSErrorCode.FALLBACK_FAILURE.msg);
        }
        return imNoticeInfoPage;
    }

    //endregion

    //region 个人

    /**
     * 个人阅读公告
     * @param id 公告对象记录id
     */
    public IMNoticeUser getUserNoticeById(long id){
        MSResponse<IMNoticeUser> mSResponse = imFeign.getUserNoticeById(id);
        if(MSResponse.isSuccess(mSResponse)){
            return mSResponse.getData();
        }else{
            return null;
        }
    }

    /**
     * 分页查询个人公告
     * notice.userId 为个人帐号id
     */
    public Page<IMNoticeUser> getUserNoticeList(Page<IMNoticeUser> page, IMNoticeInfoSearchVM searchVM){
        Page<IMNoticeUser> imNoticeInfoPage = new Page<>();
        imNoticeInfoPage.setPageSize(page.getPageSize());
        imNoticeInfoPage.setPageNo(page.getPageNo());
        searchVM.setPage(new MSPage<>(imNoticeInfoPage.getPageNo(), imNoticeInfoPage.getPageSize()));
        try {
            MSResponse<MSPage<IMNoticeUser>> returnSystemNotice = imFeign.getUserNoticeList(searchVM);
            if (MSResponse.isSuccess(returnSystemNotice)) {
                MSPage<IMNoticeUser> data = returnSystemNotice.getData();
                imNoticeInfoPage.setCount(data.getRowCount());
                imNoticeInfoPage.setList(data.getList());
            } else {
                imNoticeInfoPage.setCount(0);
                imNoticeInfoPage.setList(Lists.newArrayList());
            }
        }catch (Exception e){
            imNoticeInfoPage.setMessage(MSErrorCode.FALLBACK_FAILURE.msg);
        }
        return imNoticeInfoPage;
    }

    /**
     * 标记单条为已读
     */
    @Transactional()
    public void markUserNoticeReaded(long userId,long id){
        MSResponse<Integer> msResponse = imFeign.markUserNoticeReaded(userId,id);
        if(!MSResponse.isSuccess(msResponse)){
            throw new RuntimeException(msResponse.getMsg());
        }
    }

    /**
     * 标记多条为已读
     * @param params HashMap类型
     * ids:公告对象记录id，多个id用逗号分隔
     * userId: 操作人
     */
    @Transactional()
    public void markMultiUserNoticeReaded(Map<String,Object> params){
        MSResponse<Integer> msResponse = imFeign.markMultiUserNoticeReaded(params);
        if(!MSResponse.isSuccess(msResponse)){
            throw new RuntimeException(msResponse.getMsg());
        }
    }

    //endregion

    //region client

    /**
     * 返回在线客户端信息
     * @param withList 返回客户端列表
     */
    public MSResponse<Map> onlineClients(String withList){
        return imFeign.clients(withList);
    }

    //endregion
}
