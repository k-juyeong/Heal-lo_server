//const domainListEl = document.getElementById('domain-list');
//const domainInputEl = document.getElementById('domain-txt');
//
//domainListEl.addEventListener('change', (event) => {
//  if (event.target.value !== "type") {
//    domainInputEl.value = event.target.value
//    domainInputEl.disabled = true
//  } else {
//    domainInputEl.value = ""
//    domainInputEl.disabled = false
//  }
//});

      let emailCk = false;
      const $confirmCode = document.getElementById('email_check');
      const $form = document.getElementById('form');
      const $confirmMail = document.getElementById('confirmMail');
      const $codeCkBtn = document.getElementById('codeCkBtn');

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
                alert("이메일 인증번호를 확인해주세요");
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
                    alert("해당 메일로 인증번호가 발송되었습니다. " + "\n 확인부탁드립니다");
                  } else {
                      throw new Error(jsonData.data);
                  }
              })
              .catch(err => console.log(err));
      }