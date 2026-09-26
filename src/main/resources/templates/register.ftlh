<#import "layout.ftlh" as l>
<@l.page 'nav.register'><section class="narrow"><h1><@l.msg 'nav.register'/></h1><p class="muted"><@l.msg 'register.help'/></p>
<form method="post" action="/register" class="stack"><@l.csrf/>
<@l.field "registrationDto.username" "field.username"/>
<@l.field "registrationDto.password" "field.password" "password"/>
<button><@l.msg 'nav.register'/></button></form></section></@l.page>
