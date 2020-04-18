//let userID=3;
let userID=document.getElementById("user_id").value;
let baseUrl = 'http://demo.codingnomads.co:8082/muttsapp';

//As soon as JS file loads, we run this function to get all the items for the sidebar
function getUserChats() {
    document.getElementById('message-wrapper').innerHTML = ""
    fetch(`${baseUrl}/users/${userID}/chats/`)
        //The info retrieved in the fetch request returns a response object.
        //The response object is assigned to the parameter in the following method as "response"
        .then(response => response.json())
        //The response object needs to be turned into a JS object for parsing. That process is above, then the result is passed to the next '.then' method

        // The object created in the last step is assigned to "dataObj", then the data object is passed to a function that handles preview box creation
        .then(dataObj => createPreviewBoxes(dataObj))
};
getUserChats();

function previewBoxClick(event) {
    let previewWrap = event.target.closest('.message-preview-box');
    let previewImg = previewWrap.querySelector('.img-wrap img');
    let chatName = previewWrap.querySelector('.message-text-wrap p');
    let headerImg = document.querySelector('#recipient-image  img');
    let headerName = document.getElementById('chat-name');
    headerImg.src = previewImg.src;
    headerName.innerHTML = chatName.innerHTML;

    // This gets the value of the "data-chat_id" attribute on the clicked element
    console.log(event.target.dataset);
    let chatID = event.target.dataset.chatId;
    let senderID = event.target.dataset.senderId;
    document.getElementById('send-message').dataset.chatId = chatID;
    document.getElementById('new-message').removeAttribute('disabled');
    //The value of "chatID" is passed to this url, to create a dynamically generated API based on which preview box is clicked
    fetch(`${baseUrl}/users/${userID}/chats/` + senderID)
         //The info retrieved in the fetch request returns a response object.
         //The response object is assigned to the parameter in the following method as "response"
        .then(response => response.json())
        //The response object needs to be turned into a JS object for parsing. That process is above, then the result is passed to the next '.then' method

        // The object created in the last step is assigned to "dataObj", then the data object is passed to a function that handles the creation of a chat message bubble
        .then(dataObj => {
            console.log(dataObj);
            createChatBubbles(dataObj)})}

//Attach a "submit" listener to the message form
let newMessageForm = document.getElementById('send-message')
newMessageForm.addEventListener('submit', function(event){
    event.preventDefault();
    let msg = document.getElementById('new-message').value;
    var msgObj = {
        senderId: userID,
        chatId: event.target.dataset.chatId,
        message:msg,
    }
    createChatBubble(msgObj);
    sendNewMessage(msgObj)
    document.getElementById('new-message').value = "";
});

/*  ===============

    These next two functions iterate over an array of objects, and pass the objects to the functions that create elements

================= */
function createChatBubbles(dataObj) {
    document.getElementById('chat-bubble-wrapper').innerHTML="";
    let messageArr = dataObj.data;
    messageArr.forEach(chat => createChatBubble(chat))
}

function createPreviewBoxes(dataObj){
    let chatsArr = dataObj.data;
    chatsArr.forEach(chat => createPreviewBox(chat))
}

/*  ===============

    These next two functions create elements on the page

================= */

/*
* This function creates a single "chat bubble" (an individual message element in the chat)
* and adds it to the page
* this function takes in one parameter, a message object
*/
const createChatBubble = (msg) => {
    //Create chat bubble wrap and the pargraph that holds the chat message
    let chatBubble = document.createElement('div');
    let sentClassName;
    if( msg.senderId === userID ){
        sentClassName = "out";
    } else {
        sentClassName = "in";
    }
    chatBubble.classList.add("chat-bubble", sentClassName);
    let paragraph = document.createElement('p');
    paragraph.innerText = msg.message;
    chatBubble.appendChild(paragraph);
    //Append the created elements to the page
    let wrapper = document.getElementById('chat-bubble-wrapper');

    wrapper.prepend(chatBubble);
}

/*
* This function creates a single "Chat Preview Box" (an individual sidebar item and its children)
* and adds it to the page
* this function takes in one parameter, a chat object
*/
 function createPreviewBox(chat) {
    //Make Wrapper Div and attach Click listener
    let previewBox = document.createElement('div');
    previewBox.classList.add('message-preview-box');
    previewBox.setAttribute('data-chatId', chat.chatId);
    previewBox.setAttribute('data-senderId',chat.senderId);
    previewBox.addEventListener('click', previewBoxClick);

    // make Image wrap, image element, and append to previewWrap
    let imageWrap = document.createElement('div');
    imageWrap.setAttribute('data-chatId', chat.chatId);
    imageWrap.setAttribute('data-senderId',chat.senderId);
    imageWrap.classList.add('img-wrap');

    let image = document.createElement('img');
    image.setAttribute('data-chatId', chat.chatId);
    image.setAttribute('data-senderId',chat.senderId);
    image.setAttribute('src', chat.photoUrl)
    image.setAttribute('alt', 'default icon')
    imageWrap.appendChild(image)
    previewBox.appendChild(imageWrap)

    //Make text wrap and paragraphs with chat name and last message, and append them to the previewWrap
    let textWrap = document.createElement('div')
    textWrap.setAttribute('data-chatId', chat.chatId);
    textWrap.setAttribute('data-senderId',chat.senderId);
    textWrap.classList.add("message-text-wrap")

    let p1 = document.createElement('p')
    p1.setAttribute('data-chatId', chat.chatId);
    p1.setAttribute('data-senderId',chat.senderId);
    p1.innerHTML = chat.chatName;

    let p2 = document.createElement('p');
    p2.setAttribute('data-chatId', chat.chatId);
    p2.setAttribute('data-senderId',chat.senderId);
    p2.innerHTML = chat.lastMessage
    textWrap.appendChild(p1)
    textWrap.appendChild(p2)
    previewBox.appendChild(textWrap)

    //Make date wrap, paragraph with date, and append them to the preview Wrap
    let dateWrap = document.createElement("div");
    dateWrap.setAttribute('data-chatId', chat.chatId);
    dateWrap.setAttribute('data-senderId',chat.senderId);
    dateWrap.classList.add("date-wrap");

    let dateP = document.createElement('p')
    dateP.setAttribute('data-chatId', chat.chatId);
    dateP.setAttribute('data-senderId',chat.senderId);
    dateP.innerHTML = new Date(chat.dateSent).toLocaleDateString();
    dateWrap.appendChild(dateP)
    previewBox.appendChild(dateWrap)

    //append all element we just create to the div with the id "message-wrapper" already on the page
    let messageWrap = document.getElementById("message-wrapper")
    messageWrap.appendChild(previewBox)
 }

 /*
 * Our first post request example
 */
function sendNewMessage(msgObj) {
    let postParams = {
       method: 'POST', // *GET, POST, PUT, DELETE, etc.
       headers: {
           'Content-Type': 'application/json; charset=UTF-8',
           'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
           'Access-Control-Allow-Origin':'*'
       },
       body: JSON.stringify(msgObj)
    }
    fetch(`${baseUrl}/users/${userID}/chat/`, postParams)
        .then(res => res.json())
        .then(res => {
            console.log(res);
            return getUserChats();
    });
}