function startsWith(prefix){
	if(typeof String.prototype.starsWith!='function'){
		String.prototype.startsWith = function(prefix){
			return this.slice(0, prefix.length)===prefix;
		};
	}
}
function endsWith(suffix){
	if(typeof String.prototype.endsWith!='function'){
		return this.indexOf(suffix,this.length-suffix.length)!==-1;
	}
}

Date.prototype.Format = function (fmt) { //author: meizz
	  var o = {
	    "M+": this.getMonth() + 1, //�·�
	    "d+": this.getDate(), //��
	    "h+": this.getHours(), //Сʱ
	    "m+": this.getMinutes(), //��
	    "s+": this.getSeconds(), //��
	    "q+": Math.floor((this.getMonth() + 3) / 3), //����
	    "S": this.getMilliseconds() //����
	  };
	  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	  for (var k in o)
	  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	  return fmt;
	};