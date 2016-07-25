/*
 Extract Demo

 extractUpload({
 browseElementId:'',
 url:'',
 filters:{},
 resize:{},
 init:function(up,param){

 },
 beforeUpload:function(up,files){

 },
 filesAdded:function(up,files){

 },
 uploadProgress:function(up,files){

 },
 error:function(up,error){

 },
 success:function(up,file,info){

 }
 });
 */
function extractUpload(options) {
    var extractUpload = new plupload.Uploader({
        runtimes: 'html5,html4,flash',
        browse_button: options.browseElementId,
        url: options.url,
        flash_swf_url: 'js/Moxie.swf',
        silverlight_xap_url: 'js/Moxie.xap',
        filters: options.filters || {},
        resize:options.resize||{}
    });

    extractUpload.bind('QueueChanged', function (uploader) {
        // 在所有的文件上传完毕时，提交表单
        uploader.start();
    });
    extractUpload.bind('Init', function (up, params) {
        //初始化
        if (options.init && $.isFunction(options.init)) {
            options.init(up, params);
        }
    });
    extractUpload.bind('BeforeUpload', function (up, file) {
        //上传之前
        if (options.beforeUpload && $.isFunction(options.beforeUpload)) {
            options.beforeUpload(up, file);
        }
    });
    extractUpload.bind('FilesAdded', function (up, files) {
        //当文件添加到上传队列后触发
        if (options.filesAdded && $.isFunction(options.filesAdded)) {
            options.filesAdded(up, files);
        }
        up.refresh();
    });
    extractUpload.bind('UploadProgress', function (up, file) {
        //上传进度改变
        if (options.uploadProgress && $.isFunction(options.uploadProgress)) {
            options.uploadProgress(up, file);
        }
    });
    extractUpload.bind('Error', function (up, err) {
        //出现错误
        if (options.error && $.isFunction(options.error)) {
            options.error(up, err);
        }
        up.refresh();
    });
    extractUpload.bind('FileUploaded', function (up, file, info) {
        //上传完毕
        if (options.success && $.isFunction(options.success)) {
            options.success(up, file, info);
        }
    });
    extractUpload.init();

    return extractUpload;
}
