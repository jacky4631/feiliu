<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="sysname" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="icon" type="java.lang.String" required="true" %>
<div class="row wrapper border-bottom white-bg page-heading">
  <section class="content-header">
    <h1>
      ${title}
    </h1>
    <ol class="breadcrumb">
      <li><span><i class="fa ${icon}"></i> ${sysname}</span></li>
      <li class="active"><strong>${title}</strong></li>
    </ol>
  </section>
</div>