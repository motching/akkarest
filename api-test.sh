#!/bin/bash

PARAM=$1

if [ "$PARAM" != "v" ]
   then
       echo -e "\e[1m\"./api-test.sh v\" to see more output\n\e[0m"
fi

function check {
    MESSAGE="\e[1m"$1"\e[0m"
    COMMAND=$2
    OUTPUT="$(eval $COMMAND)"
    EXPECTED=$3
    if [ "$PARAM" == "v" ]
        then
          echo -e $MESSAGE
          echo -e "\e[1mRequest: \e[0m"$COMMAND
          echo -e "\e[1mExpected: \e[0m"$EXPECTED
          echo -e "\e[1mResponse: \e[0m"$OUTPUT
    fi
    if [ "$OUTPUT" == "$EXPECTED" ]
        then echo -e "result: \e[32mPASSED\e[0m"
        else echo -e "result: \e[31mFAILED\e[0m"
    fi 
}

echo -e "\n\n\n\n"

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "No transactions found."

#apostrophes in curl command!
check "Replace entire transaction list:"\
      "curl -s -X PUT 'localhost:5678/sanyi/transactions/?id=8&id=9'"\
      "Transaction list uploaded: 8, 9."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "Existing transactions for sanyi: 8, 9."

#apostrophes in curl command!
check "Replace entire transaction list:"\
      "curl -s -X PUT 'localhost:5678/sanyi/transactions/?id=11&id=12&id=13'"\
      "Transaction list uploaded: 11, 12, 13."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "Existing transactions for sanyi: 11, 12, 13."

check "Queue a newtransaction, get back transId:"\
      "curl -s -X POST localhost:5678/sanyi/transactions/"\
      "Transaction queued with ID: 14."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "Existing transactions for sanyi: 11, 12, 13, 14."

check "Show specific transaction:"\
      "curl -s -s -X GET localhost:5678/sanyi/transactions/12"\
      "Showing transaction for sanyi, ID: 12."

check "Replace / modify transaction:"\
      "curl -s -X PUT localhost:5678/sanyi/transactions/11?newID=15"\
      "Transaction 11 modified. New ID: 15."

check "Execute transaction:"\
      "curl -s -X POST localhost:5678/sanyi/transactions/12"\
      "Transaction 12 executed."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "Existing transactions for sanyi: 13, 14, 15."

check "Cancel transaction:"\
      "curl -s -X DELETE localhost:5678/sanyi/transactions/13"\
      "Transaction 13 canceled."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "Existing transactions for sanyi: 14, 15."

check "Delete all transactions of Sanyi:"\
      "curl -s -X DELETE localhost:5678/sanyi/transactions/"\
      "Transaction list updated, contains no transactions now."

check "Get list of transactions:"\
      "curl -s -X GET localhost:5678/sanyi/transactions/"\
      "No transactions found."
