package com.wolfking.jeesite.ms.mapper.md;

import com.kkl.kklplus.entity.md.MDAttachment;
import com.wolfking.jeesite.modules.md.entity.MdAttachment;
import com.wolfking.jeesite.modules.sys.entity.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MDAttachmentMapper extends CustomMapper<MdAttachment, MDAttachment> {
    @Override
    public void mapAtoB(MdAttachment a, MDAttachment b, MappingContext context) {
        b.setId(a.getId());
        b.setFilePath(a.getFilePath());
        b.setCreateById(Optional.ofNullable(a.getCreateBy()).map(User::getId).orElse(null));
        b.setCreateDate(a.getCreateDate());
        b.setUpdateById(Optional.ofNullable(a.getUpdateBy()).map(User::getId).orElse(null));
        b.setUpdateDate(a.getUpdateDate());
    }

    @Override
    public void mapBtoA(MDAttachment b, MdAttachment a, MappingContext context) {
        a.setId(b.getId());
        a.setFilePath(b.getFilePath());
    }
}
