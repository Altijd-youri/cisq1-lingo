# Vulnerability Analysis

## A1:2017-Injection
This vulnerability can take many shapes. To determine to which specific risks within the category this application 
could potentially be exposed I looked up the full range of possible risk and focussed on API and data persistence with an ORM.

### Description
Injection can occur when untrusted data is sent directly or with bad sanitization to an interpreter. 
This may occur via url parameters or via any other input provide by the user or external systems. 
The content of the input is meant to trick the interpreter in directly executing the content.

This OWASP T10 vulnerability contains a wide range of possible technologies to which it can be applied. 
The main concept of the vulnerability doesn't change per technologies.

### Risk
As the interpreter is tricked into executing the content provided by the  attacker this allows the attacker 
to get unauthorized read and/or write access to the system's data.

For this project, and its technology stack the following might be exploited:
1. Injection via request json.  
   _The word field in the guess (Patch) request._  
   
   As this application doesn't contain any sensitive or personaldata and is not monetized by a business it's no 
   real problem if this happens, but it's good to take measures to minimize the risk instead of just accepting it as is.
     
   If authentication and authorization is implemented the application will also handle personaldata and 
   thus this will become a mayor risk for the data confidentiality, integrity and security.
   If monetized the platform, and the business operating it might be negatively impacted and thus this becomes a mayor risks
   that should be acted up on.

2. Injection via url parameters.  
   _The uuid parameters of the game. This parameter is fed to Hibernate ORM to retrieve a Game from the database._  
   The risk and mitigation strategy for this specific vulnerability is the same as the exploit above.

### Measures
1. Injection via request json.  
   By annotating each field of request DTOs with data validation annotations the risk is fully mitigated. 
   Request with malicious formatted data will be denied by Spring with a bad request. By applying these annotations 
   the controller checks the integrity of requests. In the domain layer extra integrity checks 
   (e.g. length, type and allowlist values) can be applied. For this application and case length and 
   type check give enough grantees about the integrity.

2. Injection via url parameters.  
   This risk is mitigated by checking the formatting of the uuid before the controller passes the request 
   to the application service. Malformed uuid's throw an IllegalArgumentException on the UUID parser 
   and result in a bad request.
   
## A9:2017-Using Components with Known Vulnerabilities
Software developers nowadays use a lot of pre-written components, this is very handy as it saves in development time and 
requires less specific knowledge about all parts of the software and systems that the application does interact with.
By relaying on components written and maintained by external parties possible vulnerabilities are introduced into the 
application.
### Description
Components used in an application run with the same privilege as the application, because these components have the same
privileges vulnerabilities in these components are just as dangerous as any other vulnerability.

### Risk
If exploited this could lead to data loss, unauthorized access to data and sometimes even server take over. By adding 
authorization and authentication some of these exploits might be prevented by these mechanisms, but a lot of these exploit 
could just bypass these mechanisms and even get access to the personaldata collected by these mechanisms.

## Measures
There are two organisations that list known vulnerabilities the CVE and NVD. With software tools that integrate into
version management en Continuous integration, delivery and deployment pipelines automatic checks can be performed on these 
known vulnerabilities.  

In this project Github Dependabot is used to automatically suggest patches and bump dependency versions if an exploit is 
found. In addition to Dependabot OWASP Maven dependency checker is used in the CI pipeline to generate reports about the state 
of exploits, on this report needs to be manually acted.
Both of these solutions depend on correct dependencies listing, this listing is take care of by using Maven (pom.xml) 
in this project. By using Maven as only source for dependencies usage of unreliable sources is prevented.
  
The IDEA used to write and maintain this project is Intellij, this IDE checks the usage of listed components and alerts
the developer if a dependency is unused, so it can be removed.

## A2:2017-Broken Authentication
This application has no authentication or authorization mechanisms in place however, the application uses url based 
identifiers to bind games to a certain player. This poses a risk to the integrity of a game state.

### Description
Application functions related to session management are often implemented incorrectly according to the OWASP. This allows
attackers to compromise password, keys, sessions tokens and unique identifiers. These can be used by the attacker to take over
accounts and/or sessions.

### Risk
Games can be identified by their UUID, this is a String of number and letters. This UUID is sent within the url foreach 
request to identify the game the action should be executed on. No authentication is implemented and thus there are no checks
in place to check if a user is allowed to make mutations to a game.  
In theory and attacker could extract the uuid from the url and use it to fabricate calls to the API and influence the state 
of a game. Probably if an attacker can monitor and extract information from the client's computer, or their
network traffic authentication wouldn't stop the attacker as access to tokens and identities is provided.

## Measures
The impact and long term effect are minimal as the application doesn't handle personaldata and isn't
monetized. So I decide to accept this risk, and the possible exploits that could happen. During the development of this
application is already chose to use UUID's instead of numeric id's as identifiers to make it almost impossible to guess a 
random game identifier and mess with the game state.