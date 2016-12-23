<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div style="margin-bottom: 8px;">
  <form class="form-inline form-profile">
    <input type="hidden" id="profileId" name="profileId" value="${profile.id}">
    <table style="width:100%;">
      <tr>
        <td style="width:445px;">
          <div class="form-group" style="padding-left:5px;">
            <button type="button" id="profile-save" style="padding: 2px 6px;" class="btn btn-xs btn-outline btn-primary">保存组属性</button>
          </div>
          <div class="checkbox checkbox-info">
            <label for="canReplaceNA">
              <form:checkbox path="profile.canReplaceNA" id="canReplaceNA" value="true"/>
              参与统付替换
            </label>
          </div>
          <div class="checkbox checkbox-info">
            <label title="漫游属性创建后不能修改, 只能删除产品组重建" for="roamable">
              <form:checkbox disabled="true" path="profile.roamable" id="roamable" value="true"/>
              全国漫游
            </label>
          </div>
          <div class="checkbox checkbox-info">
            <label for="needProtected">
              <form:checkbox path="profile.needProtected" id="needProtected" value="true"/>
              保护通道(独立授权)
            </label>
          </div>
        </td>
        <td>
          <form:select data-placeholder="选择受限地区..." id="restrictStates" path="profile.restrictStates" multiple="true" cssClass="form-control chosen">
            <form:options items="${states}" itemLabel="name" itemValue="code"/>
          </form:select>
        </td>
      </tr>
    </table>
  </form>
</div>
<table id="tab-items" class="table table-stripped table-bordered">
  <thead>
  <tr>
    <th style="width:150px;">供方编码/规格</th>
    <th style="width:240px;">产品名称</th>
    <th style="width:80px;">标价</th>
    <th style="width:75px;">折扣</th>
    <th style="width:75px;">优先级</th>
    <th style="width:75px;">采购价</th>
    <th>基础产品</th>
    <th style="width:50px;">启用</th>
    <th style="width:95px;">
      <button class="btn btn-add" style="background: transparent;padding:0;"><i class="fa fa-plus"></i> 增加产品</button>
    </th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="pack" items="${packages}">
    <tr>
      <td>
        <input type="text" name="origiId" value="${pack.origiProductId}" class="form-control input-sm">
        <input type="hidden" name="objId" value="${pack.id}">
        <input type="hidden" name="size" value="${pack.size}">
      </td>
      <td>
        <input type="text" readonly name="title" value="${pack.title}" class="form-control input-sm">
      </td>
      <td>
        <div class="input-group">
          <input type="text" readonly name="price" value="${pack.price}" class="form-control input-sm">
        </div>
      </td>
      <td>
        <input type="text" name="discount" value="${pack.discount}" onchange="calPrice(this);" class="form-control input-sm">
      </td>
      <td>
        <input type="text" name="priority" value="${pack.priority}" class="form-control input-sm">
      </td>
      <td>
        <div class="input-group">
          <input type="text" name="billAmount" value="${pack.billAmount}" readonly class="form-control input-sm">
        </div>
      </td>
      <td>
        <div class="input-group">
          <select data-placeholder="上架选择..." data-prdid='${pack.baseProductId}' name="productId" class="chosen-select input-sm">
          </select>
        </div>
      </td>
      <td style="text-align: center;">
        <label>
          <input type="checkbox"
          <c:if test="${pack.enabled}"> checked="checked" </c:if> name="enabled" value="true">
        </label>
      </td>
      <td>
        <div class="btn-group">
          <button class="btn btn-white save"><i class="fa fa-save"></i></button>
          <button class="btn btn-white remove"><i class="fa fa-trash"></i></button>
        </div>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>
<div style="padding-right: 16px;">
  <button class="btn btn-add pull-right" style="background: transparent;padding:0;"><i class="fa fa-plus"></i> 增加产品</button>
</div>
<script>
  $(function () {
    $(".chosen").chosen({});
    var options = $("#template .chosen-select option");
    $("#tab-prd").find(".chosen-select").each(function () {
      var prdid = $(this).data("prdid");
      for (var i = 0; i < options.length; i++) {
        var value = options[i].value;
        var text = options[i].text;
        var price = options[i].dataset.price;
        var size = options[i].dataset.size;
        var title = options[i].dataset.title;
        var option = $("<option>").val(value).text(text).data("price", price).data("size", size).data("title", title);
        if (prdid == value) {
          option.attr("selected", true);
        }
        $(this).append(option);
      }
      $(this).chosen().change(function (e) {
        onChooseProduct(e);
      });
    });

    $("#profile-save").on('click', function (e) {
      var id = $("#profileId").val();
      var restrictStates = [], options = $("#restrictStates")[0].options;
      for (var i = 0, len = options.length; i < len; i++) {
        if (options[i].selected) {
          restrictStates.push(options[i].value);
        }
      }
      var canReplaceNA = $('#canReplaceNA').is(':checked');
      var roamable = $('#roamable').is(':checked');
      var needProtected = $('#needProtected').is(':checked');
      $.ajax({
        type: "post",
        url: "${ctx}/oc/flowpackage/profile/update",
        traditional: true,
        data: {
          "id": id,
          "canReplaceNA": canReplaceNA,
          "roamable": roamable,
          "needProtected": needProtected,
          "restrictStates": restrictStates
        },
        success: function (data) {
          if (data.status == "success") {
            toastr.success(data.message, "提示");
          } else {
            toastr.error(data.message, "警告");
          }
        }
      });
    });
  });
</script>
