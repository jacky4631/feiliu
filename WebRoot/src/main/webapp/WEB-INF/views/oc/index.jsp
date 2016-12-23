<%@ page
  import="com.jiam365.flow.oc.dto.OCDashboard, com.jiam365.flow.oc.dto.UserBillInfo, com.jiam365.flow.server.dao.TradeLogDao, com.jiam365.flow.server.dto.TradeCountByChannelName, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <style>
    h1, h2 {
      font-size: 22px !important;
    }

    .padding-b-4 {
      padding-bottom: 4px;
      padding-top: 14px;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>

    <div class="wrapper wrapper-content">
      <div class="row">
        <div class="col-md-2">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <span class="label label-success pull-right">当天</span>
              <h5>销售额</h5>
            </div>
            <div class="ibox-content">
              <h1 class="no-margins">${dashboard.billAmount}</h1>

              <div title="成功和正在充值中交易笔数在总提交笔数中的占比" class="stat-percent font-bold text-success">${dashboard.successPercent} <i class="fa fa-bolt"></i></div>
              <small>总金额</small>
            </div>
          </div>
        </div>
        <div class="col-md-2">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <span class="label label-info pull-right">当天</span>
              <h5>总支出</h5>
            </div>
            <div class="ibox-content">
              <h1 class="no-margins">${dashboard.costAmount}</h1>

              <div class="stat-percent font-bold text-info"><i class="fa fa-level-up"></i></div>
              <small>总金额</small>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <span class="label label-primary pull-right">当天</span>
              <h5>订单总数/未回调数</h5>
            </div>
            <div class="ibox-content">

              <div class="row">
                <div class="col-md-6">
                  <h1 class="no-margins">${dashboard.totalCount}</h1>

                  <div class="font-bold text-navy">回调 ${dashboard.finishPercent} <i class="fa fa-level-up"></i>
                  </div>
                </div>
                <div class="col-md-6">
                  <h1 class="no-margins">${dashboard.pendingCount}</h1>

                  <div class="font-bold text-navy">${dashboard.pendingPercent} <i class="fa fa-level-up"></i>
                    <small>未回调</small>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>七日利润曲线</h5>

              <div class="ibox-tools">
                <span class="label label-primary">更新 <fmt:formatDate value="${dashboard.time}" pattern="yyyy-MM-dd"/></span>
              </div>
            </div>
            <div class="ibox-content no-padding">
              <div class="flot-chart" style="height: 80px; width:98%;">
                <canvas id="lineChart" height="80"></canvas>
              </div>
            </div>

          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-8">
          <div class="ibox float-e-margins">
            <div class="ibox-content">
              <div>
                 <span class="pull-right text-right">
                    <small>供应商供货量Top5 <strong>按订单数</strong></small>
                    <br/>
                 </span>

                <h3 class="no-margins">
                  销售分析
                </h3>
                <small>含正在执行交易</small>
              </div>

              <div class="m-t-sm">

                <div class="row">
                  <div class="col-md-8" style="margin-top:16px;">
                    <div class="col-lg-6">
                      <canvas height="156" width="156" id="doughnutChart" style="width: 78px; height: 78px;"></canvas>
                    </div>
                    <div class="col-lg-6">
                      <canvas height="156" width="156" id="doughnutChart2" style="width: 78px; height: 78px;"></canvas>
                    </div>
                  </div>
                  <div class="col-md-4">
                    <ul class="stat-list m-t-sm" style="margin-top: 0;">
                      <%
                        OCDashboard dashboard = (OCDashboard) request.getAttribute("dashboard");
                        List<TradeCountByChannelName> list = dashboard.getTradeCountByChannels();
                        for (TradeCountByChannelName count : list) {
                          out.println("<li><div>");
                          out.println("<span class='pull-right text-right'>");
                          out.println("<small>" + count.getChannelName() + "</small>");
                          out.println("</span>");
                          out.println("<h2 class='no-margins'>" + count.getCount() + "</h2>");

                          out.println("<div class='progress progress-mini' title='已回调" +
                              count.getFinishCount() + "笔, 未回调" +
                              (count.getCount() - count.getFinishCount()) + "笔'>"
                          );
                          out.println("<div class='progress-bar' style='width:" + count.getFinishPercent() + ";'></div>");
                          out.println("</div>");
                          out.println("</div></li>");
                        }
                      %>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="m-t-md">
                <small class="pull-right">
                  <i class="fa fa-clock-o"> </i>
                  <fmt:formatDate value="${dashboard.time}" pattern="yyyy-MM-dd HH:mm:ss"/> 更新
                </small>
                <small>
                  <strong>数据分析:</strong> 左边占比图按照销售金额统计, 右边则按照订单数量统计
                </small>
              </div>

            </div>
          </div>
        </div>
        <div class="col-lg-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <span class="label label-warning pull-right">订单状态</span>
              <h5>订单监控(数量)</h5>
            </div>
            <div class="ibox-content padding-b-4">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">移动</small>
                  <h4>${dashboard.cmccSuccess + dashboard.cmccFail + dashboard.cmccPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">成功</small>
                  <h4>${dashboard.cmccSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败</small>
                  <h4>${dashboard.cmccFail}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待</small>
                  <h4>${dashboard.cmccPending}</h4>
                </div>
              </div>
            </div>
            <div class="ibox-content padding-b-4">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">联通</small>
                  <h4>${dashboard.unicomSuccess + dashboard.unicomFail + dashboard.unicomPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">成功</small>
                  <h4>${dashboard.unicomSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败</small>
                  <h4>${dashboard.unicomFail}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待</small>
                  <h4>${dashboard.unicomPending}</h4>
                </div>
              </div>
            </div>
            <div class="ibox-content padding-b-4">
              <div class="row">
                <div class="col-xs-3">
                  <small class="stats-label">电信</small>
                  <h4>${dashboard.telecomSuccess + dashboard.telecomFail + dashboard.telecomPending}</h4>
                </div>

                <div class="col-xs-3">
                  <small class="stats-label">成功</small>
                  <h4>${dashboard.telecomSuccess}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">失败</small>
                  <h4>${dashboard.telecomFail}</h4>
                </div>
                <div class="col-xs-3">
                  <small class="stats-label">等待</small>
                  <h4>${dashboard.telecomPending}</h4>
                </div>
              </div>
            </div>
            <div class="ibox-content padding-b-4">
              <div class="row">
                <%
                  List<TradeLogDao.TradeCountByScope> tcs = dashboard.getNotFinishTradeCountByScope();
                  int count = Math.min(4, tcs.size());
                  for (int i = 0; i < count; i++) {
                    TradeLogDao.TradeCountByScope t = tcs.get(i);
                    out.println("<div class=\"col-xs-3\">");
                    out.println("  <small class=\"stats-label\">" + t.stateCode + "</small>");
                    out.println("  <h4>" + t.count + "</h4>");
                    out.println("</div>");
                  }
                %>
              </div>
            </div>
          </div>
        </div>

      </div>

      <div class="row">

        <div class="col-lg-12">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>用户消费情况</h5>

              <div class="ibox-tools">
                <a class="collapse-link">
                  <i class="fa fa-chevron-up"></i>
                </a>
                <a href="#" data-toggle="dropdown" class="dropdown-toggle">
                  <i class="fa fa-wrench"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                  <li><a href="#">默认项设置</a>
                  </li>
                </ul>
                <a class="close-link">
                  <i class="fa fa-times"></i>
                </a>
              </div>
            </div>
            <div class="ibox-content">
              <div class="row">
                <div class="col-sm-10 m-b-xs">
                  <div id="switch" class="btn-group" data-toggle="buttons">
                    <label class="btn btn-sm btn-white active"> <input type="radio" name="options" value="0"> 天 </label>
                    <label class="btn btn-sm btn-white"> <input type="radio" name="options" value="1"> 周 </label>
                    <label class="btn btn-sm btn-white"> <input type="radio" name="options" value="2"> 月 </label>
                  </div>
                </div>
                <div class="col-sm-2">
                  <div class="input-group">
                    <span class="input-group-btn">
                      <button class="btn btn-sm btn-primary" type="button"> 合计</button>
                    </span>
                    <input type="text" id="total" readonly value="<fmt:formatNumber value="${dashboard.billAmount}" type="currency" pattern="#,##0.00"/>" style="background-color: #fff;"
                           class="input-sm form-control">
                  </div>
                </div>
              </div>
              <div class="table-responsive">
                <table class="table table-striped">
                  <thead>
                  <tr>
                    <th>#</th>
                    <th>客户名称</th>
                    <th>用户名</th>
                    <th>公司名称</th>
                    <th>联系人</th>
                    <th>消费金额(元)</th>
                    <th>余额(元)</th>
                  </tr>
                  </thead>
                  <tbody id="binfo">
                  <%
                    List<UserBillInfo> userBillInfoList = (List<UserBillInfo>) request.getAttribute("userBillInfoList");
                    int idx = 0;
                    for (UserBillInfo info : userBillInfoList) {
                      out.println("<tr>");
                      out.println("<td>" + ++idx + "</td>");
                      out.println("<td>" + info.getDisplayName() + "</td>");
                      out.println("<td>" + info.getUsername() + "</td>");
                      out.println("<td>" + info.getCompany() + "</td>");
                      out.println("<td>" + info.getLinkman() + "</td>");
                      out.println("<td>" + info.getBillAmount() + "</td>");
                      out.println("<td>" + info.getBalance() + "</td>");
                      out.println("</tr>");
                    }
                  %>
                  </tbody>
                </table>
              </div>

            </div>
          </div>
        </div>

      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>

<script src="${cdn}/js/plugins/chartJs/Chart.min.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("ma-dashboard");
    $("#main-nav").addClass("white-bg");

    var lineData = {
      labels: ["", "", "", "", "", "", ""],
      datasets: [
        {
          label: "Example dataset",
          fillColor: "rgba(26,179,148,0.5)",
          strokeColor: "rgba(26,179,148,0.7)",
          pointColor: "rgba(26,179,148,1)",
          pointStrokeColor: "#fff",
          pointHighlightFill: "#fff",
          pointHighlightStroke: "rgba(26,179,148,1)",
          data: [${dashboard.profitsStr}]
        }
      ]
    };

    var lineOptions = {
      scaleShowGridLines: false,
      scaleGridLineColor: "rgba(0,0,0,.05)",
      scaleGridLineWidth: 1,
      scaleShowLabels: false,
      bezierCurve: true,
      bezierCurveTension: 0.4,
      pointDot: true,
      pointDotRadius: 4,
      pointDotStrokeWidth: 1,
      pointHitDetectionRadius: 20,
      datasetStroke: true,
      datasetStrokeWidth: 2,
      datasetFill: true,
      responsive: true
    };

    var lineCtx = document.getElementById("lineChart").getContext("2d");
    new Chart(lineCtx).Line(lineData, lineOptions);

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
    var doughnutData2 = [
      {
        value: ${dashboard.cmccCount},
        color: "#a3e1d4",
        highlight: "#1ab394",
        label: "中国移动"
      },
      {
        value: ${dashboard.telecomCount},
        color: "#dedede",
        highlight: "#1ab394",
        label: "中国电信"
      },
      {
        value: ${dashboard.unicomCount},
        color: "#b5b8cf",
        highlight: "#1ab394",
        label: "中国联通"
      }
    ];
    var doughnutOptions = {
      segmentShowStroke: true,
      segmentStrokeColor: "#fff",
      segmentStrokeWidth: 2,
      percentageInnerCutout: 45, // This is 0 for Pie charts
      animationSteps: 100,
      animationEasing: "easeOutBounce",
      animateRotate: true,
      animateScale: false
    };

    var ctx = document.getElementById("doughnutChart").getContext("2d");
    new Chart(ctx).Doughnut(doughnutData, doughnutOptions);
    ctx = document.getElementById("doughnutChart2").getContext("2d");
    new Chart(ctx).Doughnut(doughnutData2, doughnutOptions);

    function loading() {
      $('#binfo').html('<tr style="text-align:center;"><td colspan="7"><img src="${ctx}/static/img/loading.gif"/></td></tr>');
    }

    $("#switch").find("label").on('click', function (e) {
      e.preventDefault();
      var sel = $(this).find("input").val();

      $.ajax({
        url: '${ctx}/oc/index/billInfo/' + sel,
        beforeSend: loading,
        method: "get",
        success: function (data) {
          $("#binfo").html(data);
        }
      });

    });
  });
</script>
</body>
</html>