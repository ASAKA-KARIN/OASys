package cn.gson.oasys.model.dao.attendcedao;

import cn.gson.oasys.model.entity.attendce.Census;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 86156
 * 统计Service层
 */
@Service
public class CensusService {
    @Autowired
    CensusDao censusDao;

    public List<Census> getTop3EmpByDept(String startTime, String endTime) {
        return censusDao.findTop3Census(startTime, endTime);
    }

}
