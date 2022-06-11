$(function () {
    $("#refresh").show();
    $("#census").hide()
    let start = $("#start").text();
    let end = $("#end").text();
    $("#censusBtn").click(function () {
        $("#refresh").hide();
        $("#census").show();
        $.ajax(
            {
                type: "post",
                url: "/census",
                data: {
                    "startTime": start,
                    "endTime": end
                },
                success: function (data) {
                    $("#census").html(data);
                },
                error: function (data) {
                    $("#refresh").show();

                }
            }
        )
    })
    $("#excelBtn").click(function () {
        $("#refresh").hide();
        $("#census").show();
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = "/downloadExcel?startTime=" + start + "&endTime=" + end;
        $.ajax(
            {
                type: "post",
                url: "/census",
                data: {
                    "startTime": start,
                    "endTime": end
                },
                success: function (data) {
                    $("#census").html(data);
                    a.click();
                },
                error: function (data) {
                    $("#refresh").show();

                }
            }
        )
    })

})