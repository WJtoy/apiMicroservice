package com.wolfking.jeesite.ms.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.ms.service.sys.MSDictService;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MSDictUtils {

    private static MSDictService msDictService = SpringContextHolder.getBean(MSDictService.class);

    /**
     * 获得某类型下所有的字典项
     *
     * @param type 字典类型
     * @return
     */
    public static List<Dict> getDictList(String type) {
        List<Dict> values =  msDictService.findListByType(type);
        if (values.size() > 0) {
            values = values.stream().sorted(Comparator.comparing(Dict::getSort)).collect(Collectors.toList());
        }
        return values;
    }

    /**
     * 获得某类型下所有的字典项
     *
     * @param type  类型
     * @return
     */
    public static Map<String,Dict> getDictMap(String type) {
        List<Dict> dictList = msDictService.findListByType(type);
        if(ObjectUtils.isEmpty(dictList)){
            dictList.clear();
            return Maps.newHashMap();
        }
        Map<String, Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getValue, item -> item));
        dictList.clear();
        return dictMap;
    }

    /**
     * 获得字典项的标题
     *
     * @param value         字典值
     * @param type          字典类型
     * @param defaultValue  默认值
     * @return
     */
    public static String getDictLabel(String value, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (Dict dict : getDictList(type)) {
                if (value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    /**
     * 获得字典
     *
     * @param value 字典值
     * @param type  字典类型
     * @return
     */
    public static Dict getDictByValue(String value, String type) {
        Dict dict = null;
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            List<Dict> dicts = getDictList(type);
            if(dicts.size() > 0){
                dict = dicts.stream().filter(t->t.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
            }
        }
        return dict;
    }

    /**
     * 按类型和标题获得值
     *
     * @param label        标题
     * @param type         类型
     * @param defaultLabel 默认值
     * @return
     */
    public static String getDictValue(String label, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (Dict dict : getDictList(type)) {
                if (label.equalsIgnoreCase(dict.getLabel())) {
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    /**
     * 获得某类型下所有的字典
     *
     * @param typePrex 类型前缀，如judge_item_
     * @param keys key列表
     * @return
     */
    public static List<Dict> getDictList(String typePrex,String[] keys) {
        List<Dict> values = Lists.newArrayList();
        if(keys == null || keys.length==0){
            return values;
        }
        String type;
        List<Dict> list;
        for(String key:keys){
            type = typePrex+key;
            list = getDictList(type);
            if(list != null && list.size()>0){
                values.addAll(list);
            }
        }
        return values.stream().sorted(Comparator.comparing(Dict::getSort)).collect(Collectors.toList());
    }


    /**
     * 按类型返回数据字典列表,并排除except指定的列表
     * @param type
     * @param except
     * @return
     */
    public static List<Dict> getDictExceptList(String type,String except){
        boolean checkExcept = false;
        if(StringUtils.isNotBlank(except)){
            if(!StringUtils.startsWith(except, ",")){
                except = ","+except;
            }
            if(!StringUtils.endsWith(except, ",")){
                except = except.concat(",");
            }
            checkExcept = true;
        }
        List<Dict> dictList = getDictList(type);
        if(dictList.size() == 0 || !checkExcept ){
            return dictList;
        }
        else{
            List<Dict> list = Lists.newArrayList();
            for(Dict dict : dictList){
                if(!except.contains(","+dict.getValue()+",")){
                    list.add(dict);
                }
            }
            return list;
        }

    }

    /**
     * 按类型返回数据字典列表（只返回指定值）
     * @param type	数据字典类型
     * @param values 指定的值列表，用逗号分隔
     * @return
     */
    public static List<Dict> getDictInclueList(String type,String values){
        boolean checkInclude = false;
        if(StringUtils.isNotBlank(values)){
            if(!StringUtils.startsWith(values, ",")){
                values = ","+values;
            }
            if(!StringUtils.endsWith(values, ",")){
                values = values.concat(",");
            }
            checkInclude = true;
        }
        List<Dict> dictList = getDictList(type);
        if(dictList.size() == 0 || !checkInclude){
            return dictList;
        }
        else{
            List<Dict> list = Lists.newArrayList();
            for(Dict dict : dictList){
                if(values.contains(","+dict.getValue()+",")){
                    list.add(dict);
                }
            }
            return list;
        }

    }

    /**
     * 按类型返回数据字典列表（只返回指定值）
     * @param type	数据字典类型
     * @param start 开始值
     * @param end   结束值
     * 	start =null and end =null	： 	返回所有
     *  start !=null and end !=null	:	返回之间的列表
     *  start  =null and end !=null	: 	返回>=end的列表
     *  start !=null and end =null	:	返回<=start的列表
     * @return
     */
    public static List<Dict> getDictListOfRange(String type,Integer start,Integer end){
        List<Dict> dictList = getDictList(type);
        if(dictList== null || dictList.size()==0){
            return dictList;
        }
        if(start == null && end == null){
            return dictList;
        }
        //返回之间的列表
        if(start != null && end != null){
            return dictList.stream().filter(t->{
                return Integer.parseInt(t.getValue())>=start && Integer.parseInt(t.getValue())<=end;
            })
                    .collect(Collectors.toList());
        }
        //返回<=start的列表
        if(start!=null && end==null){
            return dictList.stream().filter(t->Integer.parseInt(t.getValue())<=start)
                    .collect(Collectors.toList());
        }
        //返回>=end的列表
        if(start==null && end!=null){
            return dictList.stream().filter(t->Integer.parseInt(t.getValue())>=end)
                    .collect(Collectors.toList());
        }
        return dictList;
    }

    /**
     * 获取字典单一值
     *
     * @param type
     * @param defaultValue
     * @return
     */
    public static String getDictSingleValue(String type, String defaultValue) {
        if (StringUtils.isNotBlank(type)) {
            //List<Dict> dictList = DictUtils.getDictList(type);
            List<Dict> dictList = getDictList(type);
            if (dictList.size() > 0) {
                return dictList.get(0).getValue();
            }
        }
        return defaultValue;
    }
}
