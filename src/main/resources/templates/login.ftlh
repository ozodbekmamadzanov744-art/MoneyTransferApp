<#import "layout.ftlh" as l>
<@l.page 'nav.login'><section class="narrow"><h1><@l.msg 'nav.login'/></h1><#if RequestParameters.error??><p class="alert"><@l.msg 'login.error'/></p></#if><#if RequestParameters.logout??><p class="notice"><@l.msg 'login.logout'/></p></#if><#if RequestParameters.registered??><p class="notice"><@l.msg 'login.registered'/></p></#if>
<form method="post" action="/login" class="stack"><@l.csrf/>
<label><@l.msg 'field.username'/><input name="username" required maxlength="40"  autocomplete="username"></label>
<label><@l.msg 'field.password'/><input name="password" type="password" required  autocomplete="current-password"></label>
<button><@l.msg 'nav.login'/></button></form></section></@l.page>
