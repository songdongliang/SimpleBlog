basePath = '/osf';
img_base_url= 'http://192.168.42.128/';
post_cover_thumbnail='?imageView2/2/w/500';
album_thumbnail='?imageView2/1/w/200/h/200';

function escape(v) {
    var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
    v = v.replace(/(['")-><&\\\/\.])/g, function ($0) { return entry[$0] || $0; });
    return v;
}