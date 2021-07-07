# Agenda-android-studio-v1

Projeto realizado no Android Studio para apresentar como trabalho na matéria de Android. <br />

# Trajetória

Após vários dias pesquisando e aprofundando meu conhecimento no android studio, aprendi a usar o navigation component para fazer a navegação entre telas passando argumentos através do safe args, além disso melhorei minhas habilidades em layout e comecei a programar layout através do xml e não através da guia design. <br />

Se comparado a primeira tela de login no projeto LoginScreenJAVA, já temos várias melhorias no layout e também a implementação do botão de login via facebook (funcionando), por se tratar de um projeto não lançado, apenas o desenvolvedor pode usar o login via facebook por questões de privacidade e segurança. <br />

O projeto é um CRUD funcional utilizando SQLite para armazenar os contatos localmente e utilizando MYSql para salvar os contatos no servidor do professor através da opção sincronizar contatos. <br />

Tela de login: 
<br />
![image](https://user-images.githubusercontent.com/73067717/124794613-95816000-df25-11eb-871a-00d072d8fd61.png) 
<br />
Tela de Cadastro: 
<br />
![image](https://user-images.githubusercontent.com/73067717/124795219-31ab6700-df26-11eb-89cb-1a246ced0b74.png) 
<br />
Tela inicial do app: 
<br />
![image](https://user-images.githubusercontent.com/73067717/124795371-57387080-df26-11eb-9c3b-0639a1ede1ba.png)
<br />
Perfil pessoal (buscando informações do facebook): 
<br />
![image](https://user-images.githubusercontent.com/73067717/124796228-4ccaa680-df27-11eb-8053-5f26e4b9e857.png)
<br />
Settings: 
<br /> 
![image](https://user-images.githubusercontent.com/73067717/124796376-7a175480-df27-11eb-9b0a-fa16cca10e2a.png)
<br />
<br />
# Funcionalidades
Tem vários detalhes no aplicativo, apenas a opção "Transferir contato" não funciona pois deve ser realizado via bluetooth e ainda não tenho conhecimento de tal. <br />
O app pede todas as permissões necessárias para acessar câmera, dados e internet. <br />
Dentro do seu contato, você pode ligar pra ele pelo telefone, mandar mensagem via whatsapp, sms, email, telegram e messenger (telegram e messenger não enviam mensagem direto pro usuário mas abrem os respectivos aplicativos para que você selecione uma pessoa para enviar uma mensagem). <br />
Caso troque de celular, perca seu aparelho ou desinstale o app, suas inforamções ficam salvas no banco de dados MYSql externo onde pode estar recuperando depois através do botão "Listar dados recuperáveis". Para isso tem que cadastrar o mesmo "RA" ao recadastrar sua conta, pois vai buscar seu RA no banco externo e verificar os contatos vinculados a ele. 
<br />
A senha é criptografada com Argon.
# Considerações finais
Há vários detalhes que tornam o uso bem privado apenas para apresentar como trabalho. <br />
Por exemplo: o fato de o cadastro ser armazenado no banco local faz com que quando occorer de o app ser deletado, você tem que cadastrar novamente seu usuário, caso mude o "RA", você não tem acesso aos seus contatos anteriores. <br />
Reforçando novamente, por se tratar de um trabalho da faculdade, não foi feito como se fosse um app de distribuição aberta.
<br />
É um app pessoal que testou minhas capacidades de buscar informações, aprender e fazer em java (por se tratar de uma linguagem que tive pouco contato).
