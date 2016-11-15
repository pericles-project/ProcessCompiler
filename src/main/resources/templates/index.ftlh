<#include "_head.ftl">

<div class="container">
  <h3>Please select a scenario:</h3>
  <p>
    <#list scenarios as scenario >
      <a class="btn btn-primary btn-lg" href="/demo/${scenario?url}" role="button">${scenario}</a>
    </#list>
  </p>
</div>


<#include "_tail.ftl">
