package cn.gson.oasys.model.dao.attendcedao;

import cn.gson.oasys.model.entity.attendce.Census;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CensusDao extends JpaRepository<Census, Long> {

    @Query(nativeQuery = true, value = "SELECT" + " c.*" +
            "FROM (SELECT aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name,COUNT(1) num FROM" +
            "(SELECT * FROM" +
            "(SELECT  t1.user,sign,`exit`" +
            "FROM" +
            "(SELECT  attends_time sign, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 8) as t1 JOIN" +
            "(SELECT  attends_time `exit`, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 9) as t2 ON t1.user = t2.user" +
            " WHERE " +
            "LEFT(t1.sign,10) = LEFT(t2.`exit`,10)) attend JOIN aoa_user ON aoa_user.user_id = attend.user) t2 JOIN aoa_dept ON t2.dept_id = aoa_dept .dept_id" +
            " WHERE sign > ?1  AND `exit` < ?2 " +
            "GROUP BY  aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name) as c" +
            " WHERE " +
            "(SELECT COUNT(*) FROM (SELECT aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name,COUNT(1) num FROM" +
            "(SELECT * FROM" +
            "(SELECT  t1.user,sign,`exit`" +
            "FROM" +
            "(SELECT  attends_time sign, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 8) as t1 JOIN" +
            "(SELECT  attends_time `exit`, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 9) as t2 ON t1.user = t2.user" +
            " WHERE LEFT(t1.sign,10) = LEFT(t2.`exit`,10)) attend JOIN aoa_user ON aoa_user.user_id = attend.user) t2 JOIN aoa_dept ON t2.dept_id = aoa_dept .dept_id" +
            " WHERE  sign > ?1 AND `exit` < ?2 " +
            "GROUP BY  aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name) v1 " +
            "JOIN " +
            "(SELECT aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name,COUNT(1) num FROM" +
            "(SELECT * FROM" +
            "(SELECT  t1.user,sign,`exit`" +
            "FROM" +
            "(SELECT  attends_time sign, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 8) as t1 JOIN" +
            "(SELECT  attends_time `exit`, attends_user_id `user` FROM aoa_attends_list WHERE type_id = 9) as t2 ON t1.user = t2.user" +
            " WHERE LEFT(t1.sign,10) = LEFT(t2.`exit`,10)) attend JOIN aoa_user ON aoa_user.user_id = attend.user) t2 JOIN aoa_dept ON t2.dept_id = aoa_dept .dept_id" +
            " WHERE  sign > ?1 AND `exit` < ?2 " +
            "GROUP BY  aoa_dept.dept_id,aoa_dept.dept_name,t2.user_id,t2.user_name) " +
            "v2 " +
            "ON v1.dept_id = v2.dept_id " +
            "WHERE v1.num < v2.num) < 3 " +
            "ORDER BY num DESC,dept_id ASC")
    List<Census> findTop3Census(String startTime, String endTime);
}
