<%@ page import="com.jiam365.flow.server.entity.TradeLog, com.jiam365.flow.server.entity.TradeRetry, org.apache.commons.lang3.time.DateFormatUtils, java.util.Date, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="tabs-container">
  <ul class="nav nav-tabs">
    <li class="active"><a href="#tab-detail" data-toggle="tab"><i class="fa fa-info"></i> 订单情况</a></li>
    <li class=""><a href="#tab-2" data-toggle="tab"><i class="fa fa-share-alt"></i> 历史尝试（${fn:length(retries)}）</a></li>
  </ul>
  <div class="tab-content">
    <div class="tab-pane active" id="tab-detail">
      <div class="panel-body">
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-sm-2 control-label">上游流水号</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.requestNo}" class="form-control">
            </div>

            <label class="col-sm-2 control-label">付费账号</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.displayUsername}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">充值产品</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.productName}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">运营商</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.provider}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">产品ID</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.productId}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">执行ID</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.executeProductId}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">充值号码</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.mobile}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">号码信息</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.mobileInfo}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">供应商</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.channelName}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">供应商产品ID</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.origiProductId}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">开始时间</label>

            <div class="col-sm-4">
              <input type="text" readonly value="<fmt:formatDate value="${tradeLog.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
            </div>

            <label class="col-sm-2 control-label">原价(出/进)</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.price} / ${tradeLog.executeProductPrice}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">上游完成时间</label>

            <div class="col-sm-4">
              <input type="text" readonly value="<fmt:formatDate value="${tradeLog.channelFinishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
            </div>
            <label class="col-sm-2 control-label">下游完成时间</label>

            <div class="col-sm-4">
              <input type="text" readonly value="<fmt:formatDate value="${tradeLog.finishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">上游状态</label>

            <div class="col-sm-4">
              <c:if test="${tradeLog.channelResult eq -1}">
                <small class="label label-danger">失败</small>
              </c:if>
              <c:if test="${tradeLog.channelResult eq 9}">
                <small class="label label-warning">充值中</small>
              </c:if>
              <c:if test="${tradeLog.channelResult eq 0}">
                <small class="label label-primary">成功</small>
              </c:if>
            </div>
            <label class="col-sm-2 control-label">下游状态</label>

            <div class="col-sm-4">
              <c:if test="${tradeLog.result eq -1}">
                <small class="label label-danger">失败</small>
              </c:if>
              <c:if test="${tradeLog.result eq 9}">
                <small class="label label-warning">充值中</small>
              </c:if>
              <c:if test="${tradeLog.result eq 0}">
                <small class="label label-primary">成功</small>
              </c:if>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">上游消息</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.channelMessage}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">下游消息</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.message}" class="form-control">
            </div>

          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">成本折扣</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.costDiscount}" class="form-control">
            </div>

            <label class="col-sm-2 control-label">折扣</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.billDiscount}" class="form-control">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-2 control-label">成本</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.costAmount}" class="form-control">
            </div>
            <label class="col-sm-2 control-label">计费</label>

            <div class="col-sm-4">
              <input type="text" readonly value="${tradeLog.billAmount}" class="form-control">
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-2" style="text-align:right;">
              <button type="button" class="btn btn-sm btn-outline btn-primary save-remark">更新备注</button>
            </div>
            <div class="col-sm-10">
              <input id="detail_id" type="hidden" value="${tradeLog.id}"/>
              <input name="remark" id="detail_remark" placeholder="可以在这里加入备注信息..." class="form-control" value="${tradeLog.remark}"/>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="tab-pane" id="tab-2">
      <div class="panel-body">
        <table class="table table-striped">
          <thead>
          <tr>
            <th>次序</th>
            <th>通道</th>
            <th>结束时间</th>
            <th>结果消息</th>
          </tr>
          </thead>
          <tbody>
          <%
            List<TradeRetry> retries = (List<TradeRetry>) request.getAttribute("retries");
            int i = 0;
            for (i = 0; i < retries.size(); i++) {
              TradeRetry retry = retries.get(i);

              StringBuilder tr = new StringBuilder();
              tr.append("<tr>");
              tr.append("<td>").append(i + 1).append("</td>");
              tr.append("<td>").append(retry.getChannelName()).append("</td>");
              tr.append("<td>").append(DateFormatUtils.format(retry.getFinishDate(), "yyyy-MM-dd HH:mm:ss")).append("</td>");
              tr.append("<td>").append(retry.getFailReason()).append("</td>");
              tr.append("</tr>");
              out.println(tr.toString());
            }

            TradeLog tradeLog = (TradeLog) request.getAttribute("tradeLog");
            if (tradeLog.getChannelId() != null) {
              StringBuilder tr = new StringBuilder();
              tr.append("<tr class='text-navy'>");
              tr.append("<td>").append(i + 1).append("</td>");
              tr.append("<td>").append(tradeLog.getChannelName()).append("</td>");
              Date time = tradeLog.getChannelFinishDate();
              String sTime;
              if (time == null) {
                time = tradeLog.getSubmitTime();
                sTime = time == null ? "" : "[提交时间]" + DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
              } else {
                sTime = DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
              }

              tr.append("<td>").append(sTime).append("</td>");
              tr.append("<td>").append(tradeLog.getChannelMessage() == null ? "" : tradeLog.getChannelMessage()).append("</td>");
              tr.append("</tr>");
              out.println(tr.toString());
            }
          %>
          </tbody>
        </table>
      </div>
    </div>
  </div>

</div>

