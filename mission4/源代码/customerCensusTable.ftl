<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>自定义表头</title>
    <#include "/common/commoncss.ftl">
</head>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<body>
<form class="form-inline" role="form" action="/dynamicTable" method="post" id="dynamicTable">
    <select class="form-control" name="1">
        <option value="attendsTime">考勤时间</option>
        <option value="attendHmtime" >考勤时分</option>
        <option value="weekOfday">考勤日期</option>
        <option value="attendsIp">登录IP</option>
        <option value="attendsRemark">考勤备注</option>
    </select>
    <input  id="trueSub" type="submit" hidden>
</form>
<button class="btn btn-info" id="add">新增一列</button>
<button class="btn btn-success" id="submit">提交</button>

<script type="text/javascript">
    let val = 2;
    let datas = ["attendsTime","attendHmtime","weekOfday","attendsIp","attendsRemark"];
    let names = ["考勤时间","考勤时分","考勤日期","登录IP","考勤备注"];
    $(function () {
        $("#add").click(function () {
            var  select = document.createElement("select")
            select.setAttribute("name",val);
            val = val + 1;
            for (let i = 0; i < 5; i++) {
              var ops =  document.createElement("option");
                ops.setAttribute("value",datas[i]);
                ops.setAttribute("text",names[i]);
                ops.text = names[i]
                select.append(ops);
                select.setAttribute("class","form-control");
            }
            $("#dynamicTable").append(select);
        })
        $("#submit").click(function () {
            $("#trueSub").click();
        })
    })
</script>
</body>
</html>