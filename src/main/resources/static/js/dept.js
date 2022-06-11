$(function () {
    $(".parentDept").click(function () {
        var next =  $(this).next();
        if(next.is(":hidden")){
            next.show();
        }
    })
})