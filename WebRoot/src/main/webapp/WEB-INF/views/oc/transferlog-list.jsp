<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/datepicker/datepicker3.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-file-text-o" sysname="${sysname}" title="财务日志"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">对方</div>
              <input type="text" class="form-control input-sm" id="filter_EQ_username" placeholder="用户名或供应商ID">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <input type="text" class="form-control input-sm" name="filter_GED_operateTime" id="filter_GED_operateTime" placeholder="起始时间...">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <input type="text" class="form-control input-sm" name="filter_LED_operateTime" id="filter_LED_operateTime" placeholder="结束时间...">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">运营</div>
              <input type="text" class="form-control input-sm" id="filter_EQ_operator" placeholder="输入操作者">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">科目</div>
              <select name="filter_EQ_accountingSubject" id="filter_EQ_accountingSubject" class="form-control input-sm">
                <option value="">全部</option>
                <option value="1">收入</option>
                <option value="9">支出</option>
                <option value="102">内部转账</option>
                <option value="103">平账</option>
                <option value="104">罚款收入</option>
                <option value="105">被罚支出</option>
                <option value="106">投诉退款</option>
                <option value="107">短信扣款</option>
              </select>
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary btn-sm">查询</button>
            <button type="button" id="reset" class="btn btn-primary btn-sm">全部</button>
            <button type="button" id="merge" class="btn btn-info btn-sm"><i class="fa fa-check"></i> 核销删除平账项</button>
          </div>
        </form>
      </div>
      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width: 20px;padding-right: 2px;">选</th>
              <th style="width: 45px;">收付</th>
              <th style="width: 150px;">用户或通道</th>
              <th>金额</th>
              <th>原余额</th>
              <th>余额</th>
              <th>操作人</th>
              <th style="width: 140px;">操作时间</th>
              <th style="width:20%;">备注</th>
              <th style="width:60px;">科目</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${cdn}/js/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/oc/transferlog-list.js"></script>
</body>
</html>
