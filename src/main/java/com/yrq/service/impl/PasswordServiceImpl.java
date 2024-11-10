package com.yrq.service.impl;

import com.yrq.dto.ChangePasswordDto;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.GuardMapper;
import com.yrq.mapper.ResidentMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.PasswordService;
import com.yrq.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @author:YangRunqi
 * @create: 2023-02-28 14:28
 * @Description:
 */
@Service
public class PasswordServiceImpl implements PasswordService {
    @Resource
    ResidentMapper residentMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    GuardMapper guardMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean changePassword(ChangePasswordDto passwordDto) {
        String oldPassword= DigestUtils.md5DigestAsHex(passwordDto.getOldPassword().getBytes());
        String newPassword=DigestUtils.md5DigestAsHex(passwordDto.getNewPassword().getBytes());
        String redisKey;
        switch (passwordDto.getPosition()) {
            case "0": case "1": case "2": case "3":
                StaffEntity staff = staffMapper.selectById(passwordDto.getEmployeeNumber());
                redisKey = "staff:" + passwordDto.getEmployeeNumber(); // Redis key for staff
                if (staff != null && staff.getPassword().equals(oldPassword)) {
                    staff.setPassword(newPassword);
                    staffMapper.updateById(staff);
                    // 删除缓存
                    redisTemplate.delete(redisKey);
                } else {
                    return false;
                }
                break;
            case "4": case "5":
                GuardEntity guard = guardMapper.selectById(passwordDto.getEmployeeNumber());
                redisKey = "guard:" + passwordDto.getEmployeeNumber(); // Redis key for guard
                if (guard != null && guard.getPassword().equals(oldPassword)) {
                    guard.setPassword(newPassword);
                    guardMapper.updateById(guard);
                    // 删除缓存
                    redisTemplate.delete(redisKey);
                } else {
                    return false;
                }
                break;
            case "6":
                ResidentEntity resident = residentMapper.selectById(passwordDto.getIdNumber());
                redisKey = "resident:" + passwordDto.getIdNumber(); // Redis key for resident
                if (resident != null && resident.getPassword().equals(oldPassword)) {
                    resident.setPassword(newPassword);
                    residentMapper.updateById(resident);
                    // 删除缓存
                    redisTemplate.delete(redisKey);
                } else {
                    return false;
                }
                break;
        }
        return true;
    }
}
