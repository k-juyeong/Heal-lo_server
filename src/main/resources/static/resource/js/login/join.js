
      let emailCk = false;
      const $confirmCode = document.getElementById('email_check');
      const $form = document.getElementById('form');
      const $confirmMail = document.getElementById('confirmMail');
      const $codeCkBtn = document.getElementById('codeCkBtn');
      const $phoneNumber = document.getElementById('phone_number');

      $phoneNumber.addEventListener('input',(e) => {
          const length = e.target.value.length;

          if (e.inputType == "deleteContentBackward") {
               if (length == 4 || length == 9) {
                   e.target.value = e.target.value.slice(0, e.target.value.length - 1);
               }
          } else {
              if (length == 4 || length == 9) {
                  const lastIn = e.target.value.slice(e.target.value.length - 1, e.target.value.length)
                  e.target.value = e.target.value.slice(0, e.target.value.length - 1) + '-' + lastIn;
              }

              if (length == 3 || length == 8) {
                  e.target.value += '-';
              }
          }
      })

      $confirmMail.addEventListener('click',e=>{
        mailCk(e);
      });

      $codeCkBtn.addEventListener('click',e=>{
        const data = {code : $confirmCode.value}
         fetch(`/email/mailConfirm`, {
                method: 'POST',        //http method
                headers: {             //http header
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(jsonData => {
                    if (jsonData.header.code == '00') {
                        emailCk = true;
                        $confirmCode.style.border = '3px solid blue';
                        $confirmCode.disabled = true;
                    } else if(jsonData.header.code == '01'){
                        emailCk = false;
                        $confirmCode.style.border = '3px solid red';
                    } else {
                        throw new Error(jsonData.data);
                    }
                })
                .catch(err => console.log(err));
      });

      const $submitBtn = document.getElementById('submitBtn');
      $submitBtn.addEventListener('click',e=>{
            e.preventDefault();
            if(emailCk == false){
                alert("????????? ??????????????? ??????????????????");
                return;
            }else{
                $form.submit();
            }
      });

      function mailCk(e) {
          const $email_address = document.getElementById('email_address');
          const email = {email : $email_address.value}
          fetch(`/email/send`, {
              method: 'POST',        //http method
              headers: {             //http header
                  'Content-Type': 'application/json',
                  'Accept': 'application/json'
              },
              body: JSON.stringify(email)
          })
              .then(response => response.json())
              .then(jsonData => {
                    console.log(jsonData);
                  if (jsonData.header.code == '00') {
                    alert("?????? ????????? ??????????????? ?????????????????????. " + "\n ????????????????????????");
                  } else {
                      throw new Error(jsonData.data);
                  }
              })
              .catch(err => console.log(err));
      }