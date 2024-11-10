package com.yrq.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrq.dto.ResidentSearchDto;
import com.yrq.entity.*;
import com.yrq.mapper.*;
import com.yrq.service.ResidentService;
import com.yrq.utils.ConvertItem;
import com.yrq.utils.QueryPageBean;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BulkBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.swing.text.Position;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResidentServiceImpl extends ServiceImpl<ResidentMapper, ResidentEntity> implements ResidentService {
    @Resource
    ResidentMapper residentMapper;
    @Resource
    StaffMapper staffMapper;
    @Resource
    LiveMapper liveMapper;
    @Resource
    GridManageMapper gridManageMapper;
    @Resource
    BuildingMapper buildingMapper;
    @Value("${User.portrait-path.resident}")
    String basePath;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public IPage getPage(ResidentSearchDto queryPageBean){
        //设置分页
        Integer currentPage=queryPageBean.getPagination().getCurrentPage();
        Integer pageSize=queryPageBean.getPagination().getPageSize();
        Page<ResidentEntity> residentEntityIPage = new Page<>(currentPage,pageSize);
        IPage iPage=null;
        List<String> residentNumbers=getResident(queryPageBean.getPagination().getQueryString().toString(),
                queryPageBean.getBuildingNumber(),queryPageBean.getHousingState());

        LambdaQueryWrapper<ResidentEntity> qw = new LambdaQueryWrapper<>();
            if(residentNumbers.size() == 0)
                return new Page();
            qw.in(ResidentEntity::getIdNumber,residentNumbers);
            qw.like(ResidentEntity::getIdNumber,queryPageBean.getIdNumber());
            qw.like(ResidentEntity::getName,queryPageBean.getName());
            if(queryPageBean.getGender()!=null&&!queryPageBean.getGender().equals(""))
                qw.eq(ResidentEntity::getGender,queryPageBean.getGender());
            if(queryPageBean.getNation().size()>0)
                qw.in(ResidentEntity::getNation,queryPageBean.getNation());
            if(queryPageBean.getPoliticsStatus().size() > 0)
                qw.in(ResidentEntity::getPoliticsStatus,queryPageBean.getPoliticsStatus());
            if(queryPageBean.getState()!=null&&!queryPageBean.getState().equals(""))
                qw.eq(ResidentEntity::getState,queryPageBean.getState());
            if(queryPageBean.getMaxAge()!=null)
                qw.lt(ResidentEntity::getBirthDay, java.sql.Date.valueOf(LocalDate.now().minusYears(queryPageBean.getMinAge())));
            if(queryPageBean.getMinAge()!=null)
                qw.ge(ResidentEntity::getBirthDay,java.sql.Date.valueOf(LocalDate.now().minusYears(queryPageBean.getMaxAge()+1).plusDays(1)));
            iPage = residentMapper.selectPage(residentEntityIPage, qw);

        //填充剩余信息
        iPage.getRecords().forEach(resident -> {
            //计算年龄
            setLivingItems((ResidentEntity) resident);
            ConvertItem.convertResident((ResidentEntity) resident);
        });
        return iPage;
    }

    @Override
    public List<ResidentEntity> list() {
        List<ResidentEntity> residents = super.list();
        if(residents!=null){
            residents.forEach(resident -> {
                setLivingItems(resident);
                ConvertItem.convertResident(resident);
            });
        }
        return residents;
    }
    @Override
    public List<ResidentEntity> getlist(String id){
        List<String> residentNumbers=getResident(id,new ArrayList<String>(),"");
        if(residentNumbers.size()==0)
            return new ArrayList<>();
        LambdaQueryWrapper<ResidentEntity> qw = new LambdaQueryWrapper<>();
        qw.in(ResidentEntity::getIdNumber,residentNumbers);
        return residentMapper.selectList(qw);
    }
    @Override
    public ResidentEntity getById(Serializable id) {
        ResidentEntity resident = super.getById(id);
        if(resident!=null){
            setLivingItems(resident);
            ConvertItem.convertResident(resident);
        }
        return resident;
    }
    @Transactional
    @Override
    public boolean save(ResidentEntity entity) {
       entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));//密码默认6个6
       entity.setProhibitPath("default.jpg");//设置默认头像
        entity.setState("0");//初始化人员状态为正常
        if(residentMapper.selectByIdWithOutLogic(entity.getIdNumber())!=null){
            residentMapper.deleteByIdWithOutLogic(entity.getIdNumber());
        }
        //插入用户基本信息
        if(super.save(entity)){
            //插入用户住房信息
            if(entity.getBuildingNumber()!=null&&entity.getRoomNumber()!=null){
                LiveEntity liveEntity=new LiveEntity();
                liveEntity.setIdNumber(entity.getIdNumber());
                liveEntity.setBuildingNumber(entity.getBuildingNumber());
                liveEntity.setRoomNumber(entity.getRoomNumber());
                //liveEntity.setState(entity.getHousingState());
                int insert = liveMapper.insert(liveEntity);
                return insert>0;
            }
            //无住房信息回滚
            else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        if(liveMapper.deleteById(id)>0) {
            LambdaQueryWrapper<BuildingEntity> qw1=new LambdaQueryWrapper<>();
            qw1.eq(BuildingEntity::getHeaderId,id);
            BuildingEntity buildingEntity = buildingMapper.selectOne(qw1);
            if(buildingEntity!=null){
                buildingEntity.setHeaderId(null);
                buildingMapper.updateById(buildingEntity);
            }
            return super.removeById(id);
        }
        else
            return false;
    }

    @Transactional
    @Override
    public boolean updateById(ResidentEntity entity) {
        //需要修改密码时将密码先加密
        if(entity.getPassword()!=null){
            entity.setPassword( DigestUtils.md5DigestAsHex(entity.getPassword().getBytes()));
        }
        //修改基本信息
        super.updateById(entity);
        //修改住房信息
        if(entity.getBuildingNumber()!=null&&entity.getRoomNumber()!=null) {
            LiveEntity liveEntity = new LiveEntity();
            liveEntity.setIdNumber(entity.getIdNumber());
            liveEntity.setBuildingNumber(entity.getBuildingNumber());
            liveEntity.setRoomNumber(entity.getRoomNumber());
            liveEntity.setState(entity.getHousingState());
            int i = liveMapper.updateById(liveEntity);
            return i > 0;
        }
        return true;
    }
    @Override
    public boolean resetPassword(String ID){
        String redisKey;
        ResidentEntity entity=new ResidentEntity();
        entity.setIdNumber(ID);
        redisKey = "resident:" + ID;
        redisTemplate.delete(redisKey);
        entity.setPassword( DigestUtils.md5DigestAsHex("666666".getBytes()));
        return residentMapper.updateById(entity)>0;
    }



    public void setLivingItems(ResidentEntity resident){
        //合并籍贯信息
        if(resident.getNativeProvince()!=null&&resident.getNativeCity()!=null){
            resident.setNativePlace(resident.getNativeProvince()+resident.getNativeCity());
        }
        //计算年龄
        resident.setAge(ConvertItem.convertAge(resident.getBirthDay()));

        LambdaQueryWrapper<LiveEntity> qw=new QueryWrapper<LiveEntity>().lambda();
        qw.eq(LiveEntity::getIdNumber,resident.getIdNumber());
        LiveEntity live = liveMapper.selectOne(qw);

        if(live!=null){
            resident.setBuildingNumber(live.getBuildingNumber());
            resident.setRoomNumber(live.getRoomNumber());
            resident.setHousingState(live.getState());
            resident.setBuildingRoomNumber(live.getBuildingNumber()+"号"+live.getRoomNumber()+"室");
        }
    }
    private List<String> getResident(String queryString,List<String> buildingNumber,String housingState){
        if(!queryString.equals("guard")) {
            LambdaQueryWrapper<StaffEntity> qw1 = new LambdaQueryWrapper<>();
            qw1.eq(StaffEntity::getEmployeeNumber, queryString);
            Integer position = Integer.parseInt(staffMapper.selectOne(qw1).getPosition());
            if (position == 1 || position == 2) {
                //查找网格员管理的所有网格
                List<String> gridNumebers = new ArrayList<>();
                LambdaQueryWrapper<GridManageEntity> qw2 = new LambdaQueryWrapper<>();
                qw2.eq(GridManageEntity::getEmployeeNumber, queryString);
                gridManageMapper.selectList(qw2).forEach(manage -> gridNumebers.add(manage.getGridNumber()));
                if (gridNumebers.size() == 0)
                    return new ArrayList<String>();
                //查找网格内所有楼栋号
                List<String> buildingNumbers = new ArrayList<>();
                LambdaQueryWrapper<BuildingEntity> qw3 = new LambdaQueryWrapper<>();
                qw3.in(BuildingEntity::getGridNumber, gridNumebers);
                buildingMapper.selectList(qw3).forEach(building -> buildingNumbers.add(building.getBuildingNumber()));
                if (buildingNumbers.size() == 0)
                    return new ArrayList<String>();
                //查找楼栋号内所有居民
                List<String> residentNumbers = new ArrayList<>();
                LambdaQueryWrapper<LiveEntity> qw4 = new LambdaQueryWrapper<>();
                qw4.in(LiveEntity::getBuildingNumber, buildingNumbers);
                if (buildingNumber.size() > 0)
                    qw4.in(LiveEntity::getBuildingNumber, buildingNumber);
                if (housingState != "")
                    qw4.eq(LiveEntity::getState, housingState);
                liveMapper.selectList(qw4).forEach(live -> residentNumbers.add(live.getIdNumber()));
                return residentNumbers;
            }
        }
            List<String> residentNumbers = new ArrayList<>();
            LambdaQueryWrapper<LiveEntity> qw4 = new LambdaQueryWrapper<>();
            if (buildingNumber.size() > 0)
                qw4.in(LiveEntity::getBuildingNumber, buildingNumber);
            if (housingState != "")
                qw4.eq(LiveEntity::getState, housingState);
            liveMapper.selectList(qw4).forEach(live -> residentNumbers.add(live.getIdNumber()));
            return residentNumbers;

    }
}
