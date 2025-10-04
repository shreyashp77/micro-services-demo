### Steps to setup this project on local dev environment 

1. Rename the .env.sample file in the root repo to .env  
2. Add the appropriate usernames, passwords, and DB URLs.  
3. Do the same steps for the .env.sample files present in user-service, product-service, auth-service, and api-gateway projects.  
4. The .env files of api-gateway and auth-service projects contain an additional key "JWT_SECRET".  
5. It's a strong, base64-encoded secret key. This value will be loaded into the environment at runtime.  
6. It can be generated from [here](https://generate.plus/en/base64).
7. Both api-gateway and auth-service projects should have the same secret key.