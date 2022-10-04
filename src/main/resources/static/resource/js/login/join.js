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

      let emailCk = {check : false, code : ''};
      const $confirmCode = document.getElementById('email_check');
      const $form = document.getElementById('form');
      const $confirmMail = document.getElementById('confirmMail');
      $confirmMail.addEventListener('click',e=>{
        mailCk(e);
      });

      const $codeCkBtn = document.getElementById('codeCkBtn');
      $codeCkBtn.addEventListener('click',e=>{
        if(emailCk.code == '') return

        if(emailCk.code != $confirmCode.value){
            $confirmCode.style.border = '3px solid red';
            emailCk.check = false;
        }else{
            $confirmCode.style.border = '3px solid blue';
            emailCk.check = true;
            $confirmCode.disabled = true;
        }
      });

      const $submitBtn = document.getElementById('submitBtn');
      $submitBtn.addEventListener('click',e=>{
            e.preventDefault();
            if(emailCk.check == false){
                alert("이메일 인증번호를 확인해주세요");
                return;
            }else{
                $form.submit();
            }
      });

      function mailCk(e) {
          const $email_address = document.getElementById('email_address');
          const email = {email : $email_address.value}
          fetch(`/email/mailConfirm`, {
              method: 'POST',        //http method
              headers: {             //http header
                  'Content-Type': 'application/json',
                  'Accept': 'application/json'
              },
              body: JSON.stringify(email)
          })
              .then(response => response.json())
              .then(jsonData => {
                  if (jsonData.header.code == '00') {
                    alert("해당 메일로 인증번호가 발송되었습니다. " + "\n 확인부탁드립니다");
                    emailCk.code = jsonData.data.code;
                    console.log("jsonData : ",jsonData);
                  } else if (jsonData.header.code == '03') {
                  } else {
                      throw new Error(jsonData.data);
                  }
              })
              .catch(err => console.log(err));
      }