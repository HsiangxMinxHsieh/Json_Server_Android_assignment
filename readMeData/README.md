# Android assignment - Login Page

## Goal
Create a login module, that can provide the user account & password verification.

## Requirement
For making thing easier, to verify the login information by string comapre on the local.

1. Quary all the user data from RESTful API and store to the memory when the app 
start.
    * You don't need to store the data to the local sotrage, just quary them when the app start.
    * You can create your own [json-server](https://github.com/typicode/json-server) or get the data from any existing online services.
      * You need at least 8 users data.
      * Delay 4 seconds for every API calls for simulating the network delay.
      * Even though all the data is from the RESTful API now, you need to retain the flexibility for getting data from local DB in the future.
      * Provide the `db.json` if you decide to create youre own json-server localy. Make sure we can run your app on our side.
2. Compare the user account & user password
   1. Pretend the login verification is an online request even you done it locally.
   2. Delay 2.5 seconds for simulating the network delay.
3. The input user account & user password cannot be empty.
4. A simple README.md is required.
5. Try to use atomic commit, the commit message will taking into considered.
6. Put the repository on github(or bitbucket) and send the link to the HR.

## Hint
* MVVM, Data Binding, Retrofit


## In the end
This assigment will help us to know your codeing style and how you solving the problem. We don't want you spend too much time(more than 3 night) on this.


Feel free to contact our HR if you have any question.