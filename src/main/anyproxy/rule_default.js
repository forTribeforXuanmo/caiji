var utils = require("./util"),
    bodyParser = require("body-parser"),
    path = require("path"),
    fs = require("fs"),
    Promise = require("promise");

var isRootCAFileExists = require("./certMgr.js").isRootCAFileExists(),
    interceptFlag = false;
var key = "";
var uin = "";
var NowUrl = "";
//e.g. [ { keyword: 'aaa', local: '/Users/Stella/061739.pdf' } ]
var mapConfig = [],
    configFile = "mapConfig.json";
function saveMapConfig(content, cb) {
    new Promise(function (resolve, reject) {
        var anyproxyHome = utils.getAnyProxyHome(),
            mapCfgPath = path.join(anyproxyHome, configFile);

        if (typeof content == "object") {
            content = JSON.stringify(content);
        }
        resolve({
            path: mapCfgPath,
            content: content
        });
    })
        .then(function (config) {
            return new Promise(function (resolve, reject) {
                fs.writeFile(config.path, config.content, function (e) {
                    if (e) {
                        reject(e);
                    } else {
                        resolve();
                    }
                });
            });
        })
        .catch(function (e) {
            cb && cb(e);
        })
        .done(function () {
            cb && cb();
        });
}
function getMapConfig(cb) {
    var read = Promise.denodeify(fs.readFile);

    new Promise(function (resolve, reject) {
        var anyproxyHome = utils.getAnyProxyHome(),
            mapCfgPath = path.join(anyproxyHome, configFile);

        resolve(mapCfgPath);
    })
        .then(read)
        .then(function (content) {
            return JSON.parse(content);
        })
        .catch(function (e) {
            cb && cb(e);
        })
        .done(function (obj) {
            cb && cb(null, obj);
        });
}

setTimeout(function () {
    //load saved config file
    getMapConfig(function (err, result) {
        if (result) {
            mapConfig = result;
        }
    });
}, 1000);


module.exports = {
    token: Date.now(),
    summary: function () {
        var tip = "the default rule for AnyProxy.";
        if (!isRootCAFileExists) {
            tip += "\nRoot CA does not exist, will not intercept any https requests.";
        }
        return tip;
    },

    shouldUseLocalResponse: function (req, reqBody) {
        //intercept all options request
        var simpleUrl = (req.headers.host || "") + (req.url || "");
        mapConfig.map(function (item) {
            var key = item.keyword;
            if (simpleUrl.indexOf(key) >= 0) {
                req.anyproxy_map_local = item.local;
                return false;
            }
        });
        return !!req.anyproxy_map_local;
    },

    dealLocalResponse: function (req, reqBody, callback) {
        if (req.anyproxy_map_local) {
            fs.readFile(req.anyproxy_map_local, function (err, buffer) {
                if (err) {
                    callback(200, {}, "[AnyProxy failed to load local file] " + err);
                } else {
                    var header = {
                        'Content-Type': utils.contentType(req.anyproxy_map_local)
                    };
                    callback(200, header, buffer);
                }
            });
        }
    },

    replaceRequestProtocol: function (req, protocol) {

    },

    replaceRequestOption: function (req, option) {
        var newOption = option;
        if (/google/i.test(newOption.headers.host)) {
            newOption.hostname = "www.baidu.com";
            newOption.port = "80";
        }
        if (/mp\/profile_ext\?action=home/i.test(req.url) || /mp\/getmasssendmsg/i.test(req.url)) {
            if(req["headers"]["x-wechat-uin"]){
                uin = req["headers"]["x-wechat-uin"];
                key = req["headers"]["x-wechat-key"];
            }
        }
        newOption["headers"]["x-wechat-uin"] = uin;
        newOption["headers"]["x-wechat-key"] = key;


        return newOption;
    },

    replaceRequestData: function (req, data) {
    },

    replaceResponseStatusCode: function (req, res, statusCode) {
    },

    replaceResponseHeader: function (req, res, header) {

    },

    // Deprecated
    // replaceServerResData: function(req,res,serverResData){
    //     return serverResData;
    // },

    replaceServerResDataAsync: function (req, res, serverResData, callback) {

        //  if(/mp\/getmasssendmsg/i.test(req.url)){//当链接地址为公众号历史消息页面时(第一种页面形式)
        //     if(serverResData.toString() !== ""){
        //         try {//防止报错退出程序
        //
        //             var reg = /msgList = (.*?);/;//定义历史消息正则匹配规则
        //             var ret = reg.exec(serverResData.toString());//转换变量为string
        //             HttpPostAll(serverResData,ret[1],req.url,"/wx/getMsgJson");//这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器
        //             var http = require('http');
        //
        //
        //         }catch(e){//如果上面的正则没有匹配到，那么这个页面内容可能是公众号历史消息页面向下翻动的第二页，因为历史消息第一页是html格式的，第二页就是json格式的。
        //              try {
        //                 var json = JSON.parse(serverResData.toString());
        //                  HttpPostAll(serverResData,json.general_msg_list,req.url,"/wx/getMsgJson",callback);//这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
        //
        //              }catch(e){
        //                console.log(e);//错误捕捉
        // 		   callback(serverResData);//直接返回第二页json内容
        //              }
        //
        //         }
        //     }else{
        // 		console.log("data 为空");
        // 		callback(serverResData);//直接返回第二页json内容
        // 	}
        // }else
        if (/mp\/profile_ext\?action=home/i.test(req.url)) {//当链接地址为公众号历史消息页面时(第二种页面形式)
            try {

                console.log("第二种页面形式，profile_ext");
                var heads = JSON.stringify(req.headers);
                console.log("头部信息" + heads);
                var reg = /var msgList = \'(.*?)\';/;//定义历史消息正则匹配规则（和第一种页面形式的正则不同）
                var ret = reg.exec(serverResData.toString());//转换变量为string
                NowUrl = req.url;
                HttpPostAll(serverResData, ret[1], req.url, "/wx/getMsgJson", callback);//这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器

            } catch (e) {
                callback(serverResData);
            }
        } else if (/mp\/profile_ext\?action=getmsg/i.test(req.url)) {//第二种页面表现形式的向下翻页后的json
            try {
                var json = JSON.parse(serverResData.toString());
                console.log("在nodejs中一开始检测到action=getmsg获取的数据转json" + json);
                HttpPostAll(serverResData, json.general_msg_list, req.url, "/wx/getMsgJson", callback);//这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
            } catch (e) {
                console.log(e);
                callback(serverResData);
            }

        } else if (/mp\/getappmsgext/i.test(req.url)) {//当链接地址为公众号文章阅读量和点赞量时
            try {
                HttpPost(serverResData, req.url, "/wx/getMsgExt");//函数是后文定义的，功能是将文章阅读量点赞量的json发送到服务器
            } catch (e) {

            }
            callback(serverResData);
        } else if (/s\?__biz/i.test(req.url) || /mp\/rumor/i.test(req.url)) {//当链接地址为公众号文章时（rumor这个地址是公众号文章被辟谣了）
            try {

                IntoTopicPost(serverResData, req.url, "/wx/getWxPost", callback);

            } catch (e) {
                callback(serverResData);
            }
        } else {
            callback(serverResData);
        }
    },

    pauseBeforeSendingResponse: function (req, res) {
    },

    shouldInterceptHttpsReq: function (req) {
        return interceptFlag;
    },

    //[beta]
    //fetch entire traffic data
    fetchTrafficData: function (id, info) {
    },

    setInterceptFlag: function (flag) {
        interceptFlag = flag && isRootCAFileExists;
    },

    _plugIntoWebinterface: function (app, cb) {

        app.get("/filetree", function (req, res) {
            try {
                var root = req.query.root || utils.getUserHome() || "/";
                utils.filewalker(root, function (err, info) {
                    res.json(info);
                });
            } catch (e) {
                res.end(e);
            }
        });

        app.use(bodyParser.json());
        app.get("/getMapConfig", function (req, res) {
            res.json(mapConfig);
        });
        app.post("/setMapConfig", function (req, res) {
            mapConfig = req.body;
            res.json(mapConfig);

            saveMapConfig(mapConfig);
        });

        cb();
    },

    _getCustomMenu: function () {
        return [
            // {
            //     name:"test",
            //     icon:"uk-icon-lemon-o",
            //     url :"http://anyproxy.io"
            // }
        ];
    }

};
/**进入文章后代理服务器发给JAVA后台**/
function IntoTopicPost(serverResData, url, path, callback) {
    var http = require('http');
    var data = {
        url: encodeURIComponent(url)
}
    ;
    content = require('querystring').stringify(data);
    var options = {
        method: "POST",
        host: "127.0.0.1",
        port: 8088,
        path: path,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            "Content-Length": content.length
        }
    };

    var req = http.request(options, function (res) {
        var ret = /<img.*?>/g;
        var data = serverResData.toString().replace(ret, "图片");
        res.on('data', function (chunk) {
            console.log(chunk);
            callback(data + chunk);
        })
    });
    req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });
    req.write(content);
    req.end();
}


function HttpPost(str, url, path) {//将json发送到服务器，str为json内容，url为历史消息页面地址，path是接收程序的路径和文件名
    var http = require('http');
    var data = {
        str: encodeURIComponent(str),
        url: encodeURIComponent(url)
    };
    content = require('querystring').stringify(data);
    var options = {
        method: "POST",
        host: "127.0.0.1",//注意没有http://，这是服务器的域名。
        port: 8088,
        path: path,//接收程序的路径和文件名
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            "Content-Length": content.length
        }
    };
    var req = http.request(options, function (res) {
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log('BODY: ' + chunk);
        });
    });
    req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });
    req.write(content);
    req.end();
}
/***进入首页和翻页***/
function HttpPostAll(allData, str, url, path, callback) {
    var http = require('http');
    var data = {
        str: encodeURIComponent(str),
        url: encodeURIComponent(url),
        respData: allData.toString()
    };
    content = require('querystring').stringify(data);
    var options = {
        method: "POST",
        host: "127.0.0.1",//注意没有http://，这是服务器的域名。
        port: 8088,
        path: path,//接收程序的路径和文件名
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            "Content-Length": content.length
        }
    };
    var req = http.request(options, function (res) {
        res.setEncoding('utf8');

        if (/mp\/profile_ext\?action=getmsg/i.test(url)) {
            res.on('data', function (chunk) {
                console.log("内部postall中的返回的数据chunk" + chunk);
                var index = chunk.indexOf("new:");
                console.log("index" + index);
                callback(chunk);
            });
        } else {
            res.on('data', function (chunk) {
                console.log('BODY: ' + chunk);
                if (/mp\/profile_ext\?action=home/i.test(url)) {
                    var index = chunk.indexOf("new:");
                    console.log("在首页接受的 new:的位置=" + index);

                    var insert = '<script src="https://cdn.bootcss.com/jquery/2.2.0/jquery.min.js"></script>' +
                        '<script type="text/javascript"> var offset = 10;var chunk = "' + chunk + '";' +
                        'if(chunk.indexOf("new:")!=-1){window.location.href=chunk.substring(chunk.indexOf("new:")+4);} else {' +
                        'if(chunk.indexOf("OFFSET=")!=-1){offset=parseInt(chunk.substring(7,chunk.indexOf("##"))); chunk=chunk.substring(chunk.indexOf("http"));  }    var time = setInterval(function () {' +
                        '   var url=chunk.replace("_offset", offset);  console.log("url="+url);' +
                        '$.ajax({ type:"get", url:url, dataType:"text", success:function (data) {   var index=data.indexOf("new:");if(index!=-1) {window.location.href = data.substring(index + 4);}},' +
                        'error:function(XMLHttpRequest, textStatus, errorThrown) {  }, complete:function(XHR,TS) {  } });' +
                        ' offset += 10;' +
                        '}, 5000); }</script>';

                    callback(allData + insert);
                } else {
                    callback(allData);
                }
            });

        }
    });
    req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });
    req.write(content);
    req.end();
}



	
