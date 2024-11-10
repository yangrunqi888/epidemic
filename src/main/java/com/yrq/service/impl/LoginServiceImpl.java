package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrq.dto.LoginEntity;
import com.yrq.entity.GuardEntity;
import com.yrq.entity.LiveEntity;
import com.yrq.entity.ResidentEntity;
import com.yrq.entity.StaffEntity;
import com.yrq.mapper.GuardMapper;
import com.yrq.mapper.LiveMapper;
import com.yrq.mapper.ResidentMapper;
import com.yrq.mapper.StaffMapper;
import com.yrq.service.LoginService;
import com.yrq.utils.R;
import com.yrq.utils.RHttpStatusEnum;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    ResidentMapper residentMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    GuardMapper guardMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper
    @Autowired
    private MemcachedClient memcachedClient;
    @Override
    public ResidentEntity residentLogin(LoginEntity login) {
        String password = login.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 尝试从Redis中查找
        String redisKey = "resident:" + login.getIdNumber();
        String redisPassword = redisTemplate.opsForValue().get(redisKey);

        if (redisPassword != null && redisPassword.equals(password)) {
            // 从Redis中获取用户实体
            String residentJson = redisTemplate.opsForValue().get(redisKey + ":entity");
            try {
                return objectMapper.readValue(residentJson, ResidentEntity.class); // 反序列化
            } catch (Exception e) {
                e.printStackTrace(); // 处理异常
            }
        }

        // 从数据库中查找
        LambdaQueryWrapper<ResidentEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ResidentEntity::getIdNumber, login.getIdNumber());
        ResidentEntity resi = residentMapper.selectOne(qw);

        if (resi == null) {
            return null;
        }
        if (!password.equals(resi.getPassword())) {
            return null;
        }

        // 设置房间号
        LiveEntity liveEntity = liveMapper.selectOne(new LambdaQueryWrapper<LiveEntity>().eq(LiveEntity::getIdNumber, resi.getIdNumber()));
        resi.setBuildingRoomNumber(liveEntity.getBuildingNumber() + "号" + liveEntity.getRoomNumber() + "室");

        // 将用户信息存入Redis
        redisTemplate.opsForValue().set(redisKey, password, 10, TimeUnit.MINUTES);

        return resi;
    }

    @Override
    public StaffEntity staffLogin(LoginEntity login) {
        String password = login.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        String memcachedKey = "staff:" + login.getIdNumber();
        try {
            // 从Memcached中获取密码
            String memcachedPassword = (String) memcachedClient.get(memcachedKey);
            if (memcachedPassword != null && memcachedPassword.equals(password)) {
                // 从Memcached中获取用户实体
                String staffJson = (String) memcachedClient.get(memcachedKey + ":entity");
                if (staffJson != null) {
                    return objectMapper.readValue(staffJson, StaffEntity.class); // 反序列化
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 处理异常
        }

        // 从数据库查询用户信息
        LambdaQueryWrapper<StaffEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(StaffEntity::getEmployeeNumber, login.getIdNumber());
        StaffEntity staff = staffMapper.selectOne(qw);

        if (staff == null) {
            return null;
        }
        if (!password.equals(staff.getPassword())) {
            return null;
        }

        try {
            // 将用户密码存入Memcached，设置超时时间为10分钟
            memcachedClient.set(memcachedKey, (int) TimeUnit.MINUTES.toSeconds(10), password);

        } catch (Exception e) {
            e.printStackTrace(); // 处理异常
        }

        return staff;
    }
//    public StaffEntity staffLogin(LoginEntity login) {
//        String password = login.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//
//        String redisKey = "staff:" + login.getIdNumber();
//        String redisPassword = redisTemplate.opsForValue().get(redisKey);
//
//        if (redisPassword != null && redisPassword.equals(password)) {
//            // 从Redis中获取用户实体
//            String staffJson = redisTemplate.opsForValue().get(redisKey + ":entity");
//            try {
//                return objectMapper.readValue(staffJson, StaffEntity.class); // 反序列化
//            } catch (Exception e) {
//                e.printStackTrace(); // 处理异常
//            }
//        }
//
//        LambdaQueryWrapper<StaffEntity> qw = new LambdaQueryWrapper<>();
//        qw.eq(StaffEntity::getEmployeeNumber, login.getIdNumber());
//        StaffEntity staff = staffMapper.selectOne(qw);
//
//        if (staff == null) {
//            return null;
//        }
//        if (!password.equals(staff.getPassword())) {
//            return null;
//        }
//
//        // 将用户信息存入Redis
//        redisTemplate.opsForValue().set(redisKey, password, 10, TimeUnit.MINUTES);
//
//
//        return staff;
//    }

    @Override
    public GuardEntity guardLogin(LoginEntity login) {
        String password = login.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        String redisKey = "guard:" + login.getIdNumber();
        String redisPassword = redisTemplate.opsForValue().get(redisKey);

        if (redisPassword != null && redisPassword.equals(password)) {
            // 从Redis中获取用户实体
            String guardJson = redisTemplate.opsForValue().get(redisKey + ":entity");
            try {
                return objectMapper.readValue(guardJson, GuardEntity.class); // 反序列化
            } catch (Exception e) {
                e.printStackTrace(); // 处理异常
            }
        }

        LambdaQueryWrapper<GuardEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(GuardEntity::getGuardNumber, login.getIdNumber());
        GuardEntity guard = guardMapper.selectOne(qw);

        if (guard == null) {
            return null;
        }
        if (!password.equals(guard.getPassword())) {
            return null;
        }

        // 将用户信息存入Redis
        redisTemplate.opsForValue().set(redisKey, password, 10, TimeUnit.MINUTES);

        return guard;
    }
}
