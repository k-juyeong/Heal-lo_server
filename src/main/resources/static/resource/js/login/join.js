const domainListEl = document.getElementById('domain-list');
const domainInputEl = document.getElementById('domain-txt');

domainListEl.addEventListener('change', (event) => {
  if (event.target.value !== "type") {
    domainInputEl.value = event.target.value
    domainInputEl.disabled = true
  } else {
    domainInputEl.value = ""
    domainInputEl.disabled = false
  }
});