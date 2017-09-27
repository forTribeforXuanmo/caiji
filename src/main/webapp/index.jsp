<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <script src="resources/js/jquery.min.js"></script>
</head>
<body>
<div>
<button type="button" id="s" onclick="start();">启动新浪微博定时爬取</button>
<br/>
<small>每隔2小时爬取一次</small>
</div>
<div>
<button type="button" id="e" onclick="stop();">停止新浪微博爬取</button>
</div>
<textarea name="t" id="t" cols="60" rows="30"></textarea>
<div id="weibo" style="background-color: #fff3ca;border:1px solid  #000000;"></div>

<script type="text/javascript">

    function start() {
        $("#s").attr({"disabled":"disabled"});
        $('#s').html("正在爬取..");
        $.ajax({
            url: "/wb/startQuartz",
            type: 'post',
            dataType: "text",
            success: function (data) {
                $('#t').text("停止"+data);
            }
        })
    }
    function stop(){
        $.ajax({
            url:"/wb/stopQuartz",
            type:'post',
            dataType:'text',
            success:function (data) {
                $('#t').text("停止"+data);
            }
        })
    }

</script>
<link href="https://code.fancygrid.com/fancy.min.css" rel="stylesheet">
<script src="https://code.fancygrid.com/fancy.min.js"></script>

<div id="container"></div>
<script>

    $(function() {
        new FancyGrid({
            title: 'Server Paging and Sorting',
            renderTo: 'container',
            width: 700,
            height: 400,
            data: {
                proxy: {
                    url: '/caiji/wb/showJob',
                    reader: {
                    root:'data'
                    }
                }
            },
            defaults: {
                type: 'string',
                width: 100,
                resizable: true,
                sortable: true
            },
            paging: true,
            columns: [{
                index: 'jobId',
                locked: true,
                title: 'Company'
            },{
                index: 'jobName',
                title: 'Name'
            },{
                index: 'jobGroup',
                title: 'Sur Name'
            },{
                index: 'jobStatus',
                width: 60,
                title: 'Age',
            },{
                index: 'cronExpression',
                locked: true,
                ellipsis: true,
                title: 'Position',
                width: 150
            }]
        });

    });


</script>



</body>
</html>


