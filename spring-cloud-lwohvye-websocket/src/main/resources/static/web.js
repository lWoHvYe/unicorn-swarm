// 打开一个 web socket
var ws = new WebSocket("ws://(网关路径)/(服务名称)/websocket?user=Tony");
ws.onopen = function() {
    // Web Socket 已连接上，使用 send() 方法发送数据
    ws.send("测试发送");
    console.log('open');
};
ws.onmessage = function (e) {
    console.log('message', e.data);
};
ws.onclose = function() {
    console.log('close');
};
