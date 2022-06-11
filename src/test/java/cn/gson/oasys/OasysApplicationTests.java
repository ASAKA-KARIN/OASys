package cn.gson.oasys;

import java.util.List;
import java.util.Map;

import cn.gson.oasys.model.dao.attendcedao.CensusDao;
import cn.gson.oasys.model.entity.attendce.Census;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.gson.oasys.mappers.NoticeMapper;
import cn.gson.oasys.model.entity.system.SystemStatusList;
import cn.gson.oasys.services.inform.InformService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OasysApplication.class)
public class 	OasysApplicationTests {
	
	@Autowired
	private NoticeMapper nm;
	
	@Autowired
	private InformService informService;

	@Autowired
	private CensusDao censusDao;
	

		
//		List<Map<String, Object>> list=informService.informList(listOne);
//		for (Map<String, Object> map : list) {
//			System.out.println(map);
//		}
	@Test
	public void testCensus(){
		List<Census> top3Census = censusDao.findTop3Census("2017-7-10", "2020-1-1");
		for(Census census : top3Census){
			System.out.println(census);
		}
	}
	
	

}
