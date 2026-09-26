<#import "layout.ftlh" as l><@l.page 'transaction.title'><h1><@l.msg 'transaction.title'/> #${tx.id}</h1><section><dl>
<dt><@l.msg 'field.type'/></dt><dd><@l.msg 'type.'+tx.type/></dd>
<dt><@l.msg 'field.status'/></dt><dd><span class="badge ${tx.status?lower_case}"><@l.msg 'status.'+tx.status/></span></dd>
<dt><@l.msg 'field.date'/></dt><dd>${tx.createdLabel}</dd><dt><@l.msg 'field.processed'/></dt><dd>${tx.processedLabel}</dd>
<dt><@l.msg 'field.source'/></dt><dd><#if tx.sourceAccountId??>${tx.sourceAccountId} (${tx.senderUsername})<#else><@l.msg 'party.external'/></#if></dd>
<dt><@l.msg 'field.target'/></dt><dd><#if tx.targetAccountId??>${tx.targetAccountId} (${tx.recipientUsername})<#elseif tx.providerName??>${tx.providerName}: ${tx.subscriberReference}</#if></dd>
<dt><@l.msg 'field.amount'/></dt><dd>${tx.amount?string['0.00']} ${tx.currency}</dd><dt><@l.msg 'field.credited'/></dt><dd>${tx.creditedAmount?string['0.00']} ${tx.creditedCurrency}</dd><dt><@l.msg 'field.rate'/></dt><dd>1 ${tx.currency} = ${tx.rate?string['0.########']} ${tx.creditedCurrency}</dd>
<#if tx.reviewerUsername??><dt><@l.msg 'field.reviewer'/></dt><dd>${tx.reviewerUsername}</dd></#if>
<#if tx.failureKey??><dt><@l.msg 'field.reason'/></dt><dd><@l.msg tx.failureKey/></dd></#if></dl>
<#if (adminView!false) && tx.status=='PENDING'><div class="actions"><form method="post" action="/admin/transactions/${tx.id}/approve"><@l.csrf/><button><@l.msg 'action.approve'/></button></form><form method="post" action="/admin/transactions/${tx.id}/reject"><@l.csrf/><button class="danger"><@l.msg 'action.reject'/></button></form></div></#if>
</section></@l.page>
