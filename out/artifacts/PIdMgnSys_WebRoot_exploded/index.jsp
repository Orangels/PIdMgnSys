<%@ page language="java" contentType="text/html;charset=GBK" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh">
	<head>
		<title>��֤��һ����ϵͳ</title>
		<meta charset="GBK" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/bootstrap-responsive.min.css" />
		<link rel="stylesheet" href="css/datepicker.css" />
        <link rel="stylesheet" href="css/jquery-ui.css" />
		<link rel="stylesheet" href="css/uniform.css" />
		<link rel="stylesheet" href="css/select2.css" />		
		<link rel="stylesheet" href="css/unicorn.main.css" />
		<link rel="stylesheet" href="css/unicorn.grey.css" class="skin-color" />	
		
		<script src="js/my.js"></script>
		<script src="js/jquery.min.js"></script>
        
		<script type="text/javascript">
			var pageindex = 0;
			var pagecount = 4;
			$(document).ready(function() {
				var formatted_date = new Date().Format("yyyy-MM-dd");
				$('#visitdate')[0].value = formatted_date;
			});
			function resetdate() {
				$('#visitdate')[0].value = "";
			}
			function nextpage(){
				pageindex++;
				queryid(pageindex);
			}
			function previous(){
				pageindex--;
				queryid(pageindex);
			}
			
			function queryid(pindex) {
				$('tbody').empty();
				$('#visitquery').ajaxSubmit(
						{
							type : 'post',
							url : '<%=basePath%>queryid.do',
							dataType:'json',
							data:{qdate:$('#visitdate').val(),qname:$('#visitor').val(),pageindex:pindex,pagecount:pagecount},
							success:function(data){
							if(typeof(data.error)!='undefined'){
								if(data.error!=''){
									alert(data.error);
								}
							}else{
								var $tbody = $("#qresult");
								if(pageindex==0){
									$('#previous').attr("disabled", true);
								}else{
									$('#previous').removeAttr("disabled");
								}
								if(data.length==pagecount){
									$('#nextpage').removeAttr("disabled");
								}else{
									//no more pages
									$('#nextpage').attr("disabled", true);
								}
								$.each(data,function(i,item){
									var vtr = "<tr class='gradeA'>";
									vtr+="<td style='text-align:center'>"+item.visitorname+"</td>";
									vtr+="<td style='text-align:center'>"+item.gentor+"</td>";
									vtr+="<td style='text-align:center'>"+item.visitdate+"</td>";
									vtr+="<td style='text-align:center'>"+item.similar+"</td>";
									vtr+="</tr>";
									$tbody.append(vtr);
								});
							}
						},
						error:function(data,status,e){
							alert(data);
						}
					});
			}
		</script>
	</head>
	
	<body>	
		<div id="header">
			<h1>��֤��һ����ϵͳ</h1>	
		</div>
		<div id="search">
			<input type="text" placeholder="Search here..."/><button type="submit" class="tip-right"><i class="icon-search icon-white"></i></button>
		</div>
		<div id="user-nav" class="navbar navbar-inverse">
            <ul class="nav btn-group">
                <li class="btn btn-inverse" ><a title="" href="#"><i class="icon icon-user"></i> <span class="text">��������</span></a></li>
                <li class="btn btn-inverse dropdown" id="menu-messages"><a href="#" data-toggle="dropdown" data-target="#menu-messages" class="dropdown-toggle"><i class="icon icon-envelope"></i> <span class="text">��������</span> <span class="label label-important">3</span> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a class="sAdd" title="" href="#">��������</a></li>
                        <li><a class="sInbox" title="" href="#">��������</a></li>
                        <li><a class="sOutbox" title="" href="#">�Ѱ�����</a></li>
                    </ul>
                </li>
                <li class="btn btn-inverse"><a title="" href="#"><i class="icon icon-cog"></i> <span class="text">ϵͳ����</span></a></li>
                <li class="btn btn-inverse"><a title="" href="#"><i class="icon icon-share-alt"></i> <span class="text">�ǳ�</span></a></li>
            </ul>
        </div>
		<div id="sidebar">
			<ul>
				<li><a href="index.jsp"><i class="icon icon-home"></i> <span>�ÿͼ�¼</span></a></li>
				<li class="submenu">
					<a href="#"><i class="icon icon-th-list"></i> <span>���ݹ���</span></a>
					<ul>
						<li><a href="#">�û�����</a></li>
						<li><a href="#">����������</a></li>
						<li><a href="#">����������</a></li>
					</ul>
				</li>
				<li><a href="#"><i class="icon icon-pencil"></i> <span>ͳ�Ʒ���</span></a></li>
			</ul>
		</div>
		
		<div id="content">
			<div id="content-header">
				<h1>�ÿͼ�¼</h1>
			</div>
			<div id="breadcrumb">
				<a href="index.jsp" title="Go to Home" class="tip-bottom"><i class="icon-home"></i> Home</a>
				<a href="#" class="tip-bottom">�ÿͼ�¼</a>
			</div>
			<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box">
						<div class="widget-title">
							<span class="icon"> <i class="icon-align-justify"></i> </span>
							<h5>��ѯ����</h5>
						</div>
						<div class="widget-content nopadding">
							<form action="" method="post" class="form-horizontal" id="visitquery">
								<div class="control-group">
									<label class="control-label">�������ڣ�</label>
                                       <div class="controls">
                                           <input id="visitdate" readOnly="true" type="text"  data-date-format="yyyy-mm-dd" class="datepicker" />
                                      		<button type="button" onclick="resetdate();">�ÿ�</button>
                                       </div>
                                       
								</div>
								<div class="control-group">
									<label class="control-label">�ÿ�������</label>
                                       <div class="controls">
                                           <input id="visitor" type="text" />
                                       </div>
								</div>
								<div class="form-actions" align="right">
									<button type="button" class="btn btn-primary" onclick="queryid(0);">��ѯ</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
				<div class="row-fluid">
					<div class="span12">
						<div class="widget-box">
							<div class="widget-title">
								<span class="icon">
									<i class="icon-th"></i>
								</span>
								<h5>��������</h5>
							</div>
							<div class="widget-content nopadding">
								<table id="resulttable" class="table table-bordered table-striped table-hover">
									<thead>
									<tr>
									<th>�ÿ�����</th>
									<th>�Ա�</th>
									<th>����ʱ��</th>
									<th>ƥ����</th>
									</tr>
									</thead>
									<tbody id="qresult">
									</tbody>
									</table> 
							</div>
							<div align="right" >
								<div class="btn-group" >
									<button class="btn" id="previous" onclick="previous();">��һҳ</button>
									<button class="btn" id="nextpage" onclick="nextpage();">��һҳ</button>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				
				<div class="row-fluid" >
					<div id="footer" class="span12">
						2016 - 2017 &copy; �������������Ƽ����޹�˾
					</div>
				</div>
			</div>
		</div>
		
			<script src="js/jquery-form.js"></script>
            <script src="js/jquery-ui.custom.js"></script>
            <script src="js/bootstrap.min.js"></script>
            <script src="js/bootstrap-colorpicker.js"></script>
            <script src="js/bootstrap-datepicker.js"></script>
            <script src="js/jquery.uniform.js"></script>
            <script src="js/select2.min.js"></script>
            <script src="js/unicorn.js"></script>
            <script src="js/unicorn.form_common.js"></script>
            
	</body>
</html>