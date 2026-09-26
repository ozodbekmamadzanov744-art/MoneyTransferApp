<#import "/spring.ftl" as spring>
<#macro msg code><@spring.message code=code/></#macro>
<#macro csrf><#if _csrf??><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"></#if></#macro>
<#macro page title>
<!DOCTYPE html>
<html lang="${currentLanguage!'ru'}"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title><@msg title/> · MoneyTransferApp</title><link rel="stylesheet" href="/css/style.css"></head>
<body><header><a class="brand" href="/">MoneyTransferApp</a><nav>
<#if signedIn!false><a href="/transfer"><@msg 'nav.transfer'/></a><a href="/payment"><@msg 'nav.payment'/></a><a href="/history"><@msg 'nav.history'/></a>
<#if isAdmin!false><a href="/admin"><@msg 'nav.admin'/></a></#if>
<form method="post" action="/logout"><@csrf/><button class="link" type="submit"><@msg 'nav.logout'/></button></form>
<#else><a href="/login"><@msg 'nav.login'/></a><a href="/register"><@msg 'nav.register'/></a></#if>
<a href="?lang=ru" lang="ru">RU</a><a href="?lang=en" lang="en">EN</a></nav></header>
<main><#if formError??><div class="alert"><@msg formError/></div></#if><#if notice??><div class="notice" role="status"><@msg notice/></div></#if><#nested></main>
<footer><@msg 'app.demo'/></footer></body></html>
</#macro>
<#macro amount><label><@msg 'field.amount'/><input name="amount" type="number" min="0.01" max="1000000000" step="0.01" required></label></#macro>
<#macro source accounts selected=0><label><@msg 'field.source'/><select name="sourceId" required><#list accounts as a><option value="${a.id}" <#if selected == a.id>selected</#if>>${a.id} · ${a.currency} · ${a.balance?string['0.00']}</option></#list></select></label></#macro>
<#macro rates rates><div class="rates"><span><@msg 'rates.label'/></span><#list rates as rate><span>1 USD = ${rate.unitsPerUsd?string['0.######']} ${rate.code}</span></#list></div></#macro>
<#macro table transactions admin=false>
<div class="table-scroll"><table><thead><tr><th>ID</th><th><@msg 'field.date'/></th><th><@msg 'field.type'/></th><th><@msg 'field.parties'/></th><th><@msg 'field.amount'/></th><th><@msg 'field.status'/></th><th><@msg 'field.details'/></th></tr></thead><tbody>
<#list transactions as t><tr><td>${t.id}</td><td>${t.createdLabel}</td><td><@msg 'type.'+t.type/></td><td>
<#if t.sourceAccountId??>${t.sourceAccountId} (${t.senderUsername})<#else><@msg 'party.external'/></#if> →
<#if t.targetAccountId??>${t.targetAccountId} (${t.recipientUsername})<#elseif t.providerName??>${t.providerName}: ${t.subscriberReference}</#if></td>
<td>${t.amount?string['0.00']} ${t.currency}<#if t.currency != t.creditedCurrency><br>→ ${t.creditedAmount?string['0.00']} ${t.creditedCurrency}</#if></td>
<td><span class="badge ${t.status?lower_case}"><@msg 'status.'+t.status/></span></td><td><a href="<#if admin>/admin</#if>/transactions/${t.id}"><@msg 'action.open'/></a></td></tr>
<#else><tr><td colspan="7"><@msg 'history.empty'/></td></tr></#list>
</tbody></table></div>
</#macro>

<#macro field path label type="text">
<label><@msg label/>
<#if type == "password"><@spring.formPasswordInput path 'required'/><#else><@spring.formInput path 'required' type/></#if>
<@spring.showErrors "<br>" "alert"/></label>
</#macro>
<#macro fieldErrors path><@spring.bind path/><@spring.showErrors "<br>" "alert"/></#macro>
