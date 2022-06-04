$(function () {
    $("#refresh").show();
    $("#census").hide()
    $("#censusBtn").click(function () {
        $("#refresh").hide();
        $("#census").show();

        let start = $("#start").text();
        let end = $("#end").text();
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

})