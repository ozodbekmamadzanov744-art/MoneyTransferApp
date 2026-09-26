<#import "layout.ftlh" as l>
<@l.page 'home.title'>
<div class="page-heading"><div><p class="eyebrow"><@l.msg 'home.eyebrow'/></p><h1><@l.msg 'home.title'/></h1></div><#if signedIn><span class="username">${currentUsername}</span></#if></div>
<@l.rates rates/>
<#if signedIn>
<div class="cards"><#list accounts as a><article class="wallet"><span>${a.currency}</span><h2>${a.balance?string['0.00']}</h2><p><@l.msg 'field.account'/>: ${a.id}</p></article><#else><p><@l.msg 'accounts.empty'/></p></#list></div>
<#if accounts?size lt 3><section><h2><@l.msg 'accounts.create'/></h2><form method="post" action="/accounts" class="inline"><@l.csrf/><label><@l.msg 'field.currency'/><select name="currency" required>
<#list rates as rate><#assign exists=false><#list accounts as a><#if a.currency==rate.code><#assign exists=true></#if></#list><#if !exists><option value="${rate.code}">${rate.code}</option></#if></#list></select></label><button><@l.msg 'action.create'/></button></form><p class="muted"><@l.msg 'accounts.initial'/></p></section></#if>
<div class="actions"><a class="button" href="/transfer"><@l.msg 'nav.transfer'/></a><a class="button secondary" href="/payment"><@l.msg 'nav.payment'/></a></div>
<#else><section class="welcome"><h2><@l.msg 'home.welcome'/></h2><p><@l.msg 'home.description'/></p><a class="button" href="/register"><@l.msg 'nav.register'/></a> <a href="/login"><@l.msg 'nav.login'/></a></section></#if>
<section><h2><@l.msg 'topup.title'/></h2><p class="muted"><@l.msg 'topup.help'/></p><form method="post" action="/topup" class="inline"><@l.csrf/><@l.field "topUpDto.accountId" "field.account"/><@l.field "topUpDto.amount" "field.amount"/><button><@l.msg 'action.topup'/></button></form><p class="muted"><@l.msg 'moderation.help'/></p></section>
<#if signedIn><section><h2><@l.msg 'nav.history'/></h2><@l.table transactions/></section></#if>
</@l.page>
