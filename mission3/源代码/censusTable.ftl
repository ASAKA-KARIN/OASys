<table class="table table-striped table-hover table-bordered table-responsive">
    <tr>
        <th>部门</th>
        <th>成员</th>
        <th>出勤次数</th>
    </tr>
    <#if emps??>
        <#list emps as emp>
            <tr>
                <td>${emp.deptName}</td>
                <td>${emp.realName}</td>
                <td>${emp.num}</td>
            </tr>
        </#list>
    <#else >

    </#if>
</table>

