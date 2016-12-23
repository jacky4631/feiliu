<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-gears" sysname="${sysname}" title="运行参数"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <c:if test="${not empty message}">
        <div id="message" class="alert alert-success">
          <button data-dismiss="alert" class="close">×</button>
            ${message}</div>
      </c:if>
      <div class="tabs-container">
        <ul class="nav nav-tabs">
          <li class="active"><a href="#tab-timeout" data-toggle="tab"> <i class="fa fa-laptop"></i> 报告超时策略</a></li>
          <li class=""><a href="#tab-limit" data-toggle="tab"><i class="fa fa-desktop"></i> 功能与限制</a></li>
          <li class=""><a href="#tab-productselection" data-toggle="tab"><i class="fa fa-random"></i> 产品选择</a></li>
          <li class=""><a href="#tab-report" data-toggle="tab"><i class="fa fa-upload"></i> 报告推送</a></li>
        </ul>
        <div class="tab-content">
          <div class="tab-pane active" id="tab-timeout">
            <div class="panel-body">
              <div class="row">
                <div class="col-lg-12">
                  <form class="form-horizontal" method="post" action="${ctx}/oc/params/timeout">
                    <div class="form-group">
                      <label class="col-sm-2 control-label">超时自动处理</label>

                      <div class="checkbox col-sm-10">
                        <label>
                          <form:checkbox path="timeoutParam.autoProcess" value="true"/>启用
                        </label>
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">超时时间(小时)</label>

                      <div class="col-sm-6">
                        <input type="text" name="timeout" value="${timeoutParam.timeout}" class="form-control">
                        <span class="help-block m-b-none">超过设置的时间后, 未收到供应商充值报告的订单就会自动产生报告</span>
                      </div>
                    </div>

                    <div class="form-group">
                      <label class="col-sm-2 control-label">自动处理策略</label>

                      <div class="col-sm-10">
                        <div>
                          <label>
                            <input type="radio" name="timeOutStrategy" value="success"
                            <c:if test="${timeoutParam.timeOutStrategy eq 'success'}"> checked</c:if>> 超时处理为成功 </label>
                        </div>
                        <div>
                          <label>
                            <input type="radio" name="timeOutStrategy" value="fail"
                            <c:if test="${timeoutParam.timeOutStrategy eq 'fail'}"> checked</c:if>> 超时处理为失败 </label>
                        </div>
                      </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <div class="col-sm-4 col-sm-offset-2">
                        <button type="submit" class="btn btn-primary">保存</button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
          <div class="tab-pane" id="tab-limit">
            <div class="panel-body">
              <div class="row">
                <div class="col-lg-12">
                  <form class="form-horizontal" method="post" action="${ctx}/oc/params/function">
                    <div class="form-group">
                      <label class="col-sm-2 control-label">限制充值</label>

                      <div class="checkbox col-sm-10">
                        <label>
                          <form:checkbox path="functionLimitParam.enableAutoLimit" value="true"/>启用
                        </label>
                        <span class="help-block m-b-none">该设置仅对移动全网产品生效</span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">限制每月最后(天)</label>

                      <div class="col-sm-6">
                        <input type="text" name="lastDays" value="${functionLimitParam.lastDays}" class="form-control">
                        <span class="help-block m-b-none">您设置该值意味着, 每月的最后的这段时间, 移动公司充值功能将会自动关闭</span>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">接口误用保护 <i class="text-blue fa fa-shield"></i></label>

                      <div class="col-sm-6">
                        <input type="text" name="limitScopeSeconds" value="${functionLimitParam.limitScopeSeconds}" class="form-control">
                        <span class="help-block m-b-none">监控时间周期(以秒为单位)</span>
                      </div>
                    </div>

                    <div class="form-group">
                      <label class="col-sm-2 control-label">充值次数上限</label>

                      <div class="col-sm-6">
                        <input type="text" name="rechargeTimesLimit" value="${functionLimitParam.rechargeTimesLimit}" class="form-control">
                        <span class="help-block m-b-none">监控周期内同一账号, WEB加API方式累计为同一号码充值次数不能超过这个设定值</span>
                      </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">代理端产品显示</label>

                      <div class="col-sm-10">
                        <div>
                          <label>
                            <input type="radio" name="productShowMode" value="all"
                            <c:if test="${functionLimitParam.productShowMode eq 'all'}"> checked</c:if>> 产品全量显示 </label>
                        </div>
                        <div>
                          <label>
                            <input type="radio" name="productShowMode" value="authed"
                            <c:if test="${functionLimitParam.productShowMode eq 'authed'}"> checked</c:if>> 仅显示授权产品 </label>
                        </div>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <div class="col-sm-4 col-sm-offset-2">
                        <button type="submit" class="btn btn-primary">保存</button>
                      </div>
                    </div>

                  </form>
                </div>
              </div>
            </div>
          </div>
          <div class="tab-pane" id="tab-productselection">
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-12">
                  <form class="form-horizontal" method="post" action="${ctx}/oc/params/product-selection">
                    <div class="form-group">
                      <label class="col-sm-2 control-label">多供应商选择</label>

                      <div class="col-sm-10">
                        <div>
                          <label>
                            <input type="radio" name="selectionMode" value="0"
                            <c:if test="${productSelectionParam.selectionMode eq 0}"> checked</c:if>> 价格-优先级组合模式 </label>
                        </div>
                        <div>
                          <label>
                            <input type="radio" name="selectionMode" value="1"
                            <c:if test="${productSelectionParam.selectionMode eq 1}"> checked</c:if>> 轮流模式 </label>
                        </div>
                            <span class="help-block m-b-none">
                              价格-优先级模式会优选价格最低折,价格相同时, 优先级数字越小越优先;
                            </span>
                            <span class="help-block m-b-none">
                              轮流模式会轮流按次数选择各供应商, 不考虑价格和每次的包大小等因素
                            </span>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">默认启用价格保护</label>

                      <div class="checkbox col-sm-10">
                        <label>
                          <form:checkbox path="productSelectionParam.enablePriceProtect" value="true"/>启用
                        </label>
                              <span class="help-block m-b-none">
                              用户提单时即进入价格保护模式, 不启用则价格保护只在重试时生效. 提单无任何通道适合, 返回产品不可用错误
                            </span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">运营用户忽略价格保护</label>

                      <div class="checkbox col-sm-10">
                        <label>
                          <form:checkbox path="productSelectionParam.ignorePriceWithOperator" value="true"/>启用
                        </label>
                        <span class="help-block m-b-none">运营充值时, 不检查价格</span>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <label class="col-sm-2 control-label">省包替换全网包</label>

                      <div class="checkbox col-sm-10">
                        <label>
                          <form:checkbox path="productSelectionParam.enableReplacement" value="true"/>启用
                        </label>
                              <span class="help-block m-b-none">
                              用户充值全网包时, 会优先被系统替换为可用的省包(系统会回避被设置保护的供应商和用户)
                            </span>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <div class="col-sm-4 col-sm-offset-2">
                        <button type="submit" class="btn btn-primary">保存</button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
          <div class="tab-pane" id="tab-report">
            <div class="panel-body">
              <div class="row">
                <div class="col-lg-12">
                  <form class="form-horizontal" method="post" action="${ctx}/oc/params/userreport">
                    <div class="form-group">
                      <label class="col-sm-2 control-label">推送(回调)重试次数</label>

                      <div class="col-sm-6">
                        <input type="text" name="retryTimes" value="${userreportParam.retryTimes}" class="form-control">
                        <span class="help-block m-b-none">失败次数达到该值之后, 不再推送</span>
                      </div>
                    </div>

                    <div class="form-group">
                      <label class="col-sm-2 control-label">失败后重试间隔(分钟)</label>

                      <div class="col-sm-6">
                        <input type="text" name="delayMinutes" value="${userreportParam.delayMinutes}" class="form-control">
                        <span class="help-block m-b-none">延时时间</span>
                      </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                      <div class="col-sm-4 col-sm-offset-2">
                        <button type="submit" class="btn btn-primary">保存</button>
                      </div>
                    </div>
                  </form>
                </div>
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
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/form-validator/jquery.form-validator.min.js"></script>
<script type="text/javascript">
  $.validate({
    lang: "zh"
  });
  $(function () {
    activeMenu("ma-config-params");
    $("#message").delay(2000).hide(300);
  });
</script>
</body>
</html>
