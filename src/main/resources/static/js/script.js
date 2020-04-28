let userID=document.getElementById("userId").value;
let baseUrl = '/users';

//As soon as JS file loads, we run this function to get all the items for the sidebar
function getUserChats() {
    document.getElementById('message-wrapper').innerHTML = ""
    fetch(`${baseUrl}/${userID}/chats/`)
        //The info retrieved in the fetch request returns a response object.
        //The response object is assigned to the parameter in the following method as "response"
        .then(response => response.json())
        //The response object needs to be turned into a JS object for parsing. That process is above, then the result is passed to the next '.then' method

        // The object created in the last step is assigned to "dataObj", then the data object is passed to a function that handles preview box creation
        .then(dataObj => {
            console.log(dataObj)
            createPreviewBoxes(dataObj)
        })
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
    let chatID = previewWrap.dataset.chatid;
    let senderID = previewWrap.dataset.senderid;
    console.log(senderID)
    document.getElementById('send-message').dataset.chatid = chatID;
    document.getElementById('new-message').removeAttribute('disabled');
    //The value of "chatID" is passed to this url, to create a dynamically generated API based on which preview box is clicked
    fetch(`${baseUrl}/${userID}/chats/` + senderID)
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
        userId: userID,
        chatId: event.target.dataset.chatid,
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
    if( msg.userId === +userID ){
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
    imageWrap.classList.add('img-wrap');

    let image = document.createElement('img');
    image.setAttribute('src', chat.photoUrl ? chat.photoUrl : "./images/defaultIcon.svg")
    image.setAttribute('alt', 'default icon')
    imageWrap.appendChild(image)
    previewBox.appendChild(imageWrap)

    //Make text wrap and paragraphs with chat name and last message, and append them to the previewWrap
    let textWrap = document.createElement('div')
    textWrap.classList.add("message-text-wrap")

    let p1 = document.createElement('p')
    p1.innerHTML = chat.chatName;

    let p2 = document.createElement('p');
    p2.innerHTML = chat.lastMessage
    textWrap.appendChild(p1)
    textWrap.appendChild(p2)
    previewBox.appendChild(textWrap)

    //Make date wrap, paragraph with date, and append them to the preview Wrap
    let dateWrap = document.createElement("div");
    dateWrap.classList.add("date-wrap");

    let dateP = document.createElement('p')
    dateP.innerHTML = new Date(chat.dateSent).toLocaleDateString();
    dateWrap.appendChild(dateP)
    previewBox.appendChild(dateWrap)

    //append all element we just create to the div with the id "message-wrapper" already on the page
    let messageWrap = document.getElementById("message-wrapper")
    messageWrap.appendChild(previewBox)
 }

// let sendMessage = document.getElementById('send-message');
// sendMessage.addEventListener('submit', function(event){
//     event.preventDefault();
//     let msg = document.getElementById('new-message').value;
//     let messageObj = {
//         message:msg,
//         userId:userID,
//         chatId:+event.target.dataset.chatId
//     }
//     createChatBubble(messageObj);
//     sendNewMessage(messageObj);
//     document.getElementById("new-message").value = " ";
// });

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
    console.log(msgObj)
    fetch(`${baseUrl}/${userID}/message`, postParams)
        .then(res => res.json())
        .then(res => {
            console.log(res);
            return getUserChats();
    });
}

let newChatBtn = document.getElementById('new-chat-btn');
let newChatModalBody = document.getElementById('new-chat-modal-body');
newChatBtn.addEventListener('click', makeNewChatForm);

function makeNewChatForm(e) {
    newChatModalBody.innerHTML = "Loading Chat Form";
    fetch(`${baseUrl}`)
        .then(res => res.json())
        .then(data => {
            console.log(data);
            let usersArray = data.data;
            let frm = document.createElement('form');
            frm.id = `new-chat-frm`;
            let formString = ``;
            formString += `<input id="new-chat-user" type="text" list="users-list" class="form-control">`;
            formString += `<datalist id="users-list">`
            usersArray.forEach(userObj => {
                formString += `<option data-value="${userObj.id}" value="${userObj.firstName} ${userObj.lastName}"></option> `
            })
            formString += `</datalist>`
            formString += `<input type="submit" class="btn btn-success">`
            frm.innerHTML = formString;
            frm.addEventListener('submit', newChatSubmit)
            newChatModalBody.innerHTML = "";
            newChatModalBody.appendChild(frm);
        })
}

function newChatSubmit(e){
    e.preventDefault()
    let options = document.getElementById('users-list').options;
    console.log(document.getElementById('users-list').options)
    console.log(e.target.elements)
    let val = e.target.elements["new-chat-user"].value
    console.log(val)
    let newChatUserId;
    Array.from(options).forEach(option => {
        if (option.value === val) {
            newChatUserId = option.getAttribute('data-value');
        }
    })
    console.log(newChatUserId)

    fetch(baseUrl + "/" + senderID)
    .then(response => response.json())

}
