package cn.gson.oasys.model.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.gson.oasys.model.entity.user.Dept;

public interface DeptDao extends PagingAndSortingRepository<Dept, Long>{


	@Query("select d from Dept d where d.parentId = :id")
	List<Dept> findByDeptId(@Param("id") Long id);

	@Query("select d from Dept  d where d.deptId = :deptId")
	Dept findDeptByDid(@Param("deptId") Long deptId);
	
	@Query("select de.deptName from Dept de where de.deptId=:id")
	String findname(@Param("id")Long id);
}
