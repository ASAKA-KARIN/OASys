<#include "/common/commoncss.ftl">
<#include "/common/modalTip.ftl"/>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-treegrid/0.2.0/css/jquery.treegrid.min.css">
<#--<script type="text/javascript" src="js/dept.js"></script>-->
<style type="text/css">
    a {
        color: black;
    }

    a:hover {
        text-decoration: none;
    }

    .bgc-w {
        background-color: #fff;
    }
</style>
<div class="row" style="padding-top: 10px;">
    <div class="col-md-2">
        <h1 style="font-size: 24px; margin: 0;" class="">部门管理</h1>
    </div>
    <div class="col-md-10 text-right">
        <a href="##"><span class="glyphicon glyphicon-home"></span> 首页</a> > <a
                disabled="disabled">部门管理</a>
    </div>
</div>
<div class="row" style="padding-top: 15px;">
    <div class="col-md-12">
        <!--id="container"-->
        <div class="bgc-w box box-primary">
            <!--盒子头-->
            <div class="box-header">
                <h3 class="box-title">
                    <a href="deptedit?flag=1" class="label label-success" style="padding: 5px;">
                        <span class="glyphicon glyphicon-plus"></span> 新增
                    </a>
                    <#if err??>
                        <span style="color:red;">${err}</span>
                    </#if>
                </h3>
                <!-- <div class="box-tools">
                    <div class="input-group" style="width: 150px;">
                        <input type="text" class="form-control input-sm"
                            placeholder="查找..." />
                        <div class="input-group-btn">
                            <a class="btn btn-sm btn-default"><span
                                class="glyphicon glyphicon-search"></span></a>
                        </div>
                    </div>
                </div> -->
            </div>
            <!--盒子身体-->
            <div class="box-body no-padding">
                <div class="table-responsive">
                    <#--                    <table class="table table-hover table-striped">-->
                    <#--                        <tr>-->
                    <#--                            <th scope="col">名称</th>-->
                    <#--                            <th scope="col">电话</th>-->
                    <#--                            <th scope="col">邮箱</th>-->
                    <#--                            <th scope="col">地址</th>-->
                    <#--                            <th scope="col">操作</th>-->
                    <#--                        </tr>-->
                    <#--                        &lt;#&ndash;						<#list depts as dept>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;							<tr>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;								<td><span>${dept.deptName}</span></td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;								<td><span>${dept.deptTel}</span></td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;								<td><span>${dept.email}</span></td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;								<td><span>${dept.deptAddr}</span></td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;								<td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;									<a href="deptedit?dept=${dept.deptId}" class="label xiugai">&ndash;&gt;-->
                    <#--                        &lt;#&ndash;									<span class="glyphicon glyphicon-edit"></span> 修改</a> &ndash;&gt;-->
                    <#--                        &lt;#&ndash;									<a href="readdept?deptid=${dept.deptId}" class="label xiugai">&ndash;&gt;-->
                    <#--                        &lt;#&ndash;										<span class="glyphicon glyphicon-search"></span> 人事调动&ndash;&gt;-->
                    <#--                        &lt;#&ndash;									</a>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;									<a href="readdept?deptid=${dept.deptId}" class="label shanchu"><span&ndash;&gt;-->
                    <#--                        &lt;#&ndash;										class="glyphicon glyphicon-remove"></span> 删除</a></td>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;							</tr>&ndash;&gt;-->
                    <#--                        &lt;#&ndash;						</#list>&ndash;&gt;-->

                    <#--                        <#list depts?keys as key>-->
                    <#--                            <tr class="parentDept">-->
                    <#--                                <td><span>${key.deptName}</span></td>-->
                    <#--                                <td><span>${key.deptTel}</span></td>-->
                    <#--                                <td><span>${key.email}</span></td>-->
                    <#--                                <td><span>${key.deptAddr}</span></td>-->
                    <#--                                <td>-->
                    <#--                                    <a href="deptedit?dept=${key.deptId}" class="label xiugai">-->
                    <#--                                        <span class="glyphicon glyphicon-edit"></span> 修改</a>-->
                    <#--                                    <a href="readdept?deptid=${key.deptId}" class="label xiugai">-->
                    <#--                                        <span class="glyphicon glyphicon-search"></span> 人事调动-->
                    <#--                                    </a>-->
                    <#--                                    <a href="readdept?deptid=${key.deptId}" class="label shanchu"><span-->
                    <#--                                                class="glyphicon glyphicon-remove"></span> 删除</a></td>-->
                    <#--                            </tr>-->
                    <#--                            <#list depts?api.get(key) as son>-->
                    <#--                                <tr style="color: cornflowerblue" class="sonDept" hidden="hidden">-->
                    <#--                                    <td><span>${son.deptName}</span></td>-->
                    <#--                                    <td><span>${son.deptTel}</span></td>-->
                    <#--                                    <td><span>${son.email}</span></td>-->
                    <#--                                    <td><span>${son.deptAddr}</span></td>-->
                    <#--                                    <td>-->
                    <#--                                        <a href="deptedit?dept=${son.deptId}" class="label xiugai">-->
                    <#--                                            <span class="glyphicon glyphicon-edit"></span> 修改</a>-->
                    <#--                                        <a href="readdept?deptid=${son.deptId}" class="label xiugai">-->
                    <#--                                            <span class="glyphicon glyphicon-search"></span> 人事调动-->
                    <#--                                        </a>-->
                    <#--                                        <a href="readdept?deptid=${son.deptId}" class="label shanchu"><span-->
                    <#--                                                    class="glyphicon glyphicon-remove"></span> 删除</a></td>-->
                    <#--                                </tr>-->
                    <#--                            </#list>-->
                    <#--                        </#list>-->
                    <#--                    </table>-->
                    <table id="table"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-table/1.12.1/bootstrap-table.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-table/1.12.0/extensions/treegrid/bootstrap-table-treegrid.js"></script>
<script src="https://cdn.bootcss.com/jquery-treegrid/0.2.0/js/jquery.treegrid.min.js"></script>
<script type="text/javascript">
    var $table = $('#table');
    $(function () {
        $table.bootstrapTable({
            url: 'deptmanageRe',
            idField: 'deptId',
            parentIdField: 'parentId',
            search: true,
            treeShowField: 'deptName',
            clickToSelect: true,
            singleSelect: true,
            columns: [
                {
                    field: 'check', checkbox: true, formatter: function (value, row, index) {
                        if (row.check == true) {
                            return {checked: true};
                        }
                    }
                },
                {field: 'deptName', title: '名称'},
                {field: 'deptTel', title: '电话'},
                {field: 'email', title: '邮箱'},
                {field: 'deptAddr', title: '地址'},
                {field: 'opera', title: '操作', align: 'center', formatter: 'formatOpera'},
            ],
            onResetView: function (data) {
                $table.treegrid({
                    initialState: 'collapsed',
                    treeColumn: 0,
                    onChange: function () {
                        $table.bootstrapTable('resetWidth');
                    }
                });
                // $table.treegrid('getRootNodes').treegrid('expand');
            },
            // onCheck:function (row) {
            //     var datas = $table.bootstrapTable('getData');
            //     console.log(datas);
            //     console.log(row);
            //     selectChilds(datas,row,"deptId","parentId",true);
            //     selectParentChecked(datas,row,"deptId","parentId");
            //     $table.bootstrapTable('load',datas);
            // },
            // onUncheck:function (row) {
            //     var datas = $table.bootstrapTable('getData');
            //     console.log(datas);
            //     console.log(row);
            //     selectChilds(datas,row,"deptId","parentId",false);
            //     $table.bootstrapTable('load',datas);
            //
            // }
        });
    });

    function formatOpera(value, row, index) {
        return [

            '<a href="deptedit?flag=1&dept=' + row.deptId + '" class="label xiugai"> <span class="glyphicon glyphicon-edit"></span> 增加</a> ' +
            '<a href="deptedit?flag=0&dept=' + row.deptId + '" class="label xiugai"> <span class="glyphicon glyphicon-edit"></span> 修改</a> ' +
            '<a href="readdept?deptid=' + row.deptId + '" class="label xiugai"> <span class="glyphicon glyphicon-search"></span> 人事调动 </a> ' +
            '<a href="readdept?deptid=' + row.deptId + '" class="label shanchu"><span class="glyphicon glyphicon-remove"></span> 删除</a></td>'
        ].join(' ');
    }

    /**
     * 选中父项时，同时选中子项
     * @param datas 所有的数据
     * @param row 当前数据
     * @param id id 字段名
     * @param pid 父id字段名
     */
    function selectChilds(datas, row, id, pid, checked) {
        for (var i in datas) {
            if (datas[i][pid] == row[id]) {
                datas[i].check = checked;
                selectChilds(datas, datas[i], id, pid, checked);
            }
            ;
        }
    }

    function selectParentChecked(datas, row, id, pid) {
        for (var i in datas) {
            if (datas[i][id] == row[pid]) {
                datas[i].check = true;
                selectParentChecked(datas, datas[i], id, pid);
            }
            ;
        }
    }
</script>