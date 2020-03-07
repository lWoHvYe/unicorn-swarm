function testJson() {
    $.ajax({
        url: getRootPath() + "/jsonTest/test",
        dataType: "json",
        async: true,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(
            {
                "code": "200",
                "data": {
                    "rows": [
                        {
                            "cameraId": "100",
                            "cameraType": 0,
                            "createTime": "2019-03-05",
                            "decodetag": "dahua",
                            "extraField": {},
                            "indexCode": "1c0de121-2ce9-4b9f-b58e-bec34f66c5b2",
                            "isOnline": 1,
                            "latitude": "18.2557",
                            "longitude": "135.6358",
                            "name": "一个卡口",
                            "pixel": 1,
                            "updateTime": "2019-04-18"
                        },
                        {
                            "cameraId": "1004",
                            "cameraType": 0,
                            "createTime": "2018-07-05",
                            "decodetag": "dahua",
                            "extraField": {},
                            "indexCode": "63717de5-dd4b-4682-ae45-284d600d6cd3",
                            "isOnline": 1,
                            "latitude": "49.2123",
                            "longitude": "63.6311",
                            "name": "又一个球机",
                            "pixel": 1,
                            "updateTime": "2019-01-01"
                        },
                        {
                            "cameraId": "101",
                            "cameraType": 0,
                            "createTime": "2017-06-06",
                            "decodetag": "hikvision",
                            "extraField": {},
                            "indexCode": "",
                            "isOnline": 1,
                            "latitude": "47.2438",
                            "longitude": "89.6809",
                            "name": "一个球机",
                            "pixel": 1,
                            "updateTime": "2017-10-05"
                        },
                        {
                            "cameraId": "1063",
                            "cameraType": 0,
                            "createTime": "2019-08-15",
                            "decodetag": "hikvision",
                            "extraField": {},
                            "indexCode": "",
                            "isOnline": 1,
                            "latitude": "12.2375",
                            "longitude": "37.6782",
                            "name": "一个枪机",
                            "pixel": 1,
                            "updateTime": "2019-10-01"
                        }
                    ],
                    "page": 0,
                    "size": 4000,
                    "total": 2877
                },
                "msg": "成功"
            }
        ),
        success: function (data) {
            if (data.flag) {
                alert("提交成功");
            }
        },
        error: function () {
            alert("数据异常");
        }
    });
}

//获取项目根路径
function getRootPath() {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}