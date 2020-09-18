package com.example.service.impl

import com.example.beans.factory.annotation.Autowired
import com.example.beans.factory.annotation.Component
import com.example.dal.dao.AccountDao
import com.example.model.Account
import com.example.model.Client
import com.example.service.AccountService

@Component
class AccountServiceImpl @Autowired constructor(
    private val accountDao: AccountDao
): AccountService {
    override fun findAccounts(client: Client): List<Account> =
        accountDao.findAccounts(client)

    override fun getDefaultAccount(client: Client): Account =
        accountDao.findDefaultAccount(client)!!



}
