export default function loadPaypalScript(callback) {
  if (document.getElementById('paypal-sdk')) {
    callback();
    return;
  }

  const script = document.createElement('script');
  script.id = 'paypal-sdk';
  script.src = "https://www.paypal.com/sdk/js?client-id=sb"; // "sb" es sandbox
  script.onload = callback;
  document.body.appendChild(script);
}
