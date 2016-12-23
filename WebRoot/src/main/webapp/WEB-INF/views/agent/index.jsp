<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-diamond" sysname="${sysname}" title="概况"/>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="row">
        <div class="col-lg-7">
          <div class="ibox float-e-margins">
            <div class="ibox-content">
              <div>
                <span class="pull-right text-right">
                  <small>今日消费实况 <strong><shiro:principal property="displayName"/></strong></small>
                  <br>账户余额: ${dashboard.balance} 元
                </span>

                <h3 class="font-bold no-margins">
                  今日消费
                </h3>
                <small>流量充值</small>
              </div>

              <div class="m-t-sm">
                <div class="row">
                  <div class="col-md-8">
                    <div>
                      <canvas height="352" id="doughnutChart" style="width: 465px; height: 218px;" width="930"></canvas>
                    </div>
                  </div>
                  <div class="col-md-4">
                    <ul class="stat-list m-t-lg">
                      <li>
                        <h2 class="no-margins">${dashboard.billAmount}</h2>
                        <small>总消费量(含预扣费订单)</small>
                        <div class="progress progress-mini">
                          <div style="width: ${dashboard.finishPercent};" class="progress-bar"></div>
                        </div>
                      </li>
                      <li>
                        <h2 class="no-margins">${dashboard.totalCount}</h2>
                        <small>总订单数量</small>
                        <div class="progress progress-mini">
                          <div style="width: ${dashboard.finishPercent};" class="progress-bar"></div>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="m-t-md">
                <small class="pull-right">
                  <i class="fa fa-clock-o"> </i>
                  上次更新 <fmt:formatDate value="${dashboard.time}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </small>
                <small>
                  <strong>消费分析:</strong> 数据在不断变化, 本界面统计数据不会自动跟随, 请手工刷新.
                </small>
              </div>
            </div>
          </div>
        </div>
        <div class="col-lg-5">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <span class="label label-warning pull-right">订单状态</span>
              <h5>今日订单实时状态</h5>
            </div>
            <div class="ibox-content">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">移动总单数</small>
                  <h4>${dashboard.cmccSuccess + dashboard.cmccFail + dashboard.cmccPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">交易成功数</small>
                  <h4>${dashboard.cmccSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待数</small>
                  <h4>${dashboard.cmccPending}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败数</small>
                  <h4>${dashboard.cmccFail}</h4>
                </div>
              </div>
            </div>
            <div class="ibox-content">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">电信总单数</small>
                  <h4>${dashboard.telecomSuccess + dashboard.telecomFail + dashboard.telecomPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">交易成功数</small>
                  <h4>${dashboard.telecomSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待数</small>
                  <h4>${dashboard.telecomPending}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败数</small>
                  <h4>${dashboard.telecomFail}</h4>
                </div>
              </div>
            </div>
            <div class="ibox-content">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">联通总单数</small>
                  <h4>${dashboard.unicomSuccess + dashboard.unicomFail + dashboard.unicomPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">交易成功数</small>
                  <h4>${dashboard.unicomSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待数</small>
                  <h4>${dashboard.unicomPending}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败数</small>
                  <h4>${dashboard.unicomFail}</h4>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/chartJs/Chart.min.js"></script>
<script type="text/javascript">
  $(function () {
    $("#m-dashboard").addClass("active");

    var doughnutData = [
      {
        value: ${dashboard.cmccAmount},
        color: "#a3e1d4",
        highlight: "#1ab394",
        label: "中国移动"
      },
      {
        value: ${dashboard.telecomAmount},
        color: "#dedede",
        highlight: "#1ab394",
        label: "中国电信"
      },
      {
        value: ${dashboard.unicomAmount},
        color: "#b5b8cf",
        highlight: "#1ab394",
        label: "中国联通"
      }
    ];

    var doughnutOptions = {
      segmentShowStroke: true,
      segmentStrokeColor: "#fff",
      segmentStrokeWidth: 2,
      percentageInnerCutout: 45,
      animationSteps: 100,
      animationEasing: "easeOutBounce",
      animateRotate: true,
      animateScale: false,
      responsive: true
    };

    var ctx = document.getElementById("doughnutChart").getContext("2d");
    new Chart(ctx).Doughnut(doughnutData, doughnutOptions);
  });
</script>
</body>
</html>