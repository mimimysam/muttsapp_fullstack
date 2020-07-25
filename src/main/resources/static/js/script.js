let userId=document.getElementById("userId").value;
let baseUrl = '/muttsapp/users';
let savedUserChats = [];

var stompClient = null;

function connect() {
        var socket = new SockJS('/muttsapp/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
//            setConnected(true);
            stompClient.subscribe('/topic/messages/' + userId, function(messageOutput) {
                console.log("Notification Alert");
                getUserChats();
                otherUserId = document.getElementById('header-main').dataset.otheruserid;
                getChatMessages(otherUserId);
                changeTitle();
            });
        });
    }
connect();

function disconnect() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
//        setConnected(false);
        console.log("Disconnected");
    }


var count = 0;
var title = document.title;

function changeTitle() {
    count++;
    var newTitle = '(' + count + ') ' + title;
    document.title = newTitle;
}

function getUserImg() {
    fetch(`${baseUrl}/${userId}/`)
    .then(response => response.json())
        .then(userObj => {
            let selfImage = document.querySelector('#self-image img');
            selfImage.setAttribute('src', userObj.data.photoUrl ? userObj.data.photoUrl : "./images/defaultIcon.svg");
            selfImage.setAttribute('alt', 'user image');
        })
}
getUserImg();

let logoutBtn = document.getElementById('logout-btn');
logoutBtn.addEventListener('click', logout);

function logout(e) {
    disconnect();
    location.replace("/muttsapp/logout");
}

//As soon as JS file loads, we run this function to get all the items for the sidebar
function getUserChats() {
    document.getElementById('message-wrapper').innerHTML = ""
    fetch(`${baseUrl}/${userId}/chats/`)
        //The info retrieved in the fetch request returns a response object.
        //The response object is assigned to the parameter in the following method as "response"
        .then(response => response.json())
        //The response object needs to be turned into a JS object for parsing. That process is above, then the result is passed to the next '.then' method
        // The object created in the last step is assigned to "dataObj", then the data object is passed to a function that handles preview box creation
        .then(dataObj => {
            createPreviewBoxes(dataObj)
        })
};
getUserChats();

/*  ===============

    These next two functions iterate over an array of objects, and pass the objects to the functions that create elements

================= */
function createChatBubbles(dataObj, senderID) {
    document.getElementById('chat-bubble-wrapper').innerHTML="";
    let messageArr = dataObj.data;
    messageArr.forEach(chat => createChatBubble(chat, senderID))
}

function createPreviewBoxes(dataObj){
    document.getElementById('message-wrapper').innerHTML = ""
    let chatsArr = dataObj.data;
    let sorted = chatsArr.sort((a, b) => new Date(b.dateSent.replace(/ /g,"T")) - new Date(a.dateSent.replace(/ /g,"T")))
    chatsArr.forEach(chat => {
        savedUserChats.push(chat)
        createPreviewBox(chat)
    })
}

function previewBoxClick(event) {
    let previewWrap = event.target.closest('.message-preview-box');
    let previewImg = previewWrap.querySelector('.img-wrap img');
    let chatName = previewWrap.querySelector('.message-text-wrap p');
    let headerImg = document.querySelector('#recipient-image img');
    let headerName = document.getElementById('chat-name');
    headerImg.src = previewImg.src;
    headerName.innerHTML = chatName.innerHTML;

    let chatID = previewWrap.dataset.chatid;
    let senderID = previewWrap.dataset.senderid;
    let otherUserId = previewWrap.dataset.otheruserid;
    document.getElementById('delete-chat-btn').dataset.chatid = chatID;
    document.getElementById('send-message').dataset.chatid = chatID;
    document.getElementById('send-message').dataset.otheruserid = otherUserId;
    document.getElementById('image-form').dataset.senderid = senderID;
    document.getElementById('image-form').dataset.chatid = chatID;
    document.getElementById('image-form').dataset.otheruserid = otherUserId;
    document.getElementById('header-main').dataset.otheruserid = otherUserId;
    document.getElementById('new-message').removeAttribute('disabled');
    document.title = "Mutts App";
    count = 0;
    getChatMessages(otherUserId)
    }

function getChatMessages(senderID){
    fetch(`${baseUrl}/${userId}/chats/` + senderID)
         //The info retrieved in the fetch request returns a response object.
         //The response object is assigned to the parameter in the following method as "response"
        .then(response => response.json())
        //The response object needs to be turned into a JS object for parsing. That process is above, then the result is passed to the next '.then' method
        // The object created in the last step is assigned to "dataObj", then the data object is passed to a function that handles the creation of a chat message bubble
        .then(dataObj => {
            createChatBubbles(dataObj, senderID)})
    }

/*  ===============

    These next two functions create elements on the page

================= */

/*
* This function creates a single "chat bubble" (an individual message element in the chat)
* and adds it to the page
* this function takes in one parameter, a message object
*/
const createChatBubble = (msg, senderID) => {
    //Create chat bubble wrap and the paragraph that holds the chat message
    let chatBubble = document.createElement('div');
    let sentClassName;
    if( msg.userId === +userId ){
        sentClassName = "out";
    } else {
        sentClassName = "in";
    }
    chatBubble.classList.add("chat-bubble", sentClassName);
    chatBubble.setAttribute('id', msg.id)
    chatBubble.setAttribute('image', msg.image)
    if (msg.image === true) {
        let image = document.createElement('img')
        image.setAttribute('src', msg.message)
        image.setAttribute('alt', 'uploaded image')
        chatBubble.appendChild(image)
    } else {
        let paragraph = document.createElement('p');
        paragraph.innerText = msg.message;
        chatBubble.appendChild(paragraph);
    }
    let wrapper = document.getElementById('chat-bubble-wrapper');
    wrapper.prepend(chatBubble);
    let el = document.querySelector('#chat-scroll-wrapper');
    el.scrollTop = el.scrollHeight;
    if (sentClassName === "out") {
        chatBubble.addEventListener('click', bubbleClick);
//        chatBubble.setAttribute('data-toggle', 'modal');
//        chatBubble.setAttribute('data-target', '#deleteMessageModal');
        chatBubble.setAttribute('data-otheruserid', senderID)
    }
}

function bubbleClick(event) {
    let bubble = event.target.closest(".chat-bubble");
    let bubbleId = bubble.getAttribute('id');
    let otherSenderId = bubble.getAttribute("data-otheruserid");
//    let otherSenderId = bubble.dataset.otheruserid (same as line above, different syntax)
    deleteBtn.setAttribute('data-message_id', bubbleId);
    deleteBtn.setAttribute('data-other_user_id', otherSenderId);
    $('#deleteMessageModal').modal('show');
}

let deleteBtn = document.getElementById('delete-btn');
deleteBtn.addEventListener('click', deleteMessage);

function deleteMessage(event) {
    let msgId = event.target.dataset.message_id
    let updatedSenderId = event.target.dataset.other_user_id;
    let postParams = {
       method: 'DELETE', // *GET, POST, PUT, DELETE, etc.
       headers: {
           'Content-Type': 'application/json; charset=UTF-8',
           'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
           'Access-Control-Allow-Origin':'*'
       },
//       body: JSON.stringify()
    };
    fetch(`/muttsapp/message/${msgId}`, postParams)
//        .then(res => res.json())
        .then(res => {
            $('#deleteMessageModal').modal('hide')
            getChatMessages(updatedSenderId);
            getUserChats();
    });
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
    previewBox.setAttribute('data-otherUserId',chat.otherUserId);
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
    let dateVar = chat.dateSent.replace(/ /g,"T")
    if (dateVar != "-1") {
        dateP.innerHTML = new Date(dateVar).toLocaleDateString();
    } else {
        dateP.innerHTML = ""
    }
    dateWrap.appendChild(dateP)
    previewBox.appendChild(dateWrap)

    //append all element we just create to the div with the id "message-wrapper" already on the page
    let messageWrap = document.getElementById("message-wrapper")
    messageWrap.appendChild(previewBox)
 }

 let sendMessage = document.getElementById('send-message');
 sendMessage.addEventListener('submit', function(event){
     event.preventDefault();
     let msg = document.getElementById('new-message').value;
     let msgObj = {
         userId:+userId,
         chatId:+(event.target.dataset.chatid),
         message:msg,
     }
     createChatBubble(msgObj);
     sendNewMessage(msgObj);
     document.getElementById("new-message").value = " ";
 });

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
    };
    fetch(`${baseUrl}/${userId}/message/`, postParams)
        .then(res => res.json())
        .then(res => {
            getUserChats();
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
    let val = e.target.elements["new-chat-user"].value
    let newChatUserId;
    let found = false;
    Array.from(options).forEach(option => {
        if (option.value === val) {
            newChatUserId = option.getAttribute('data-value');
            savedUserChats.forEach(chat => {
                if (+newChatUserId === chat.otherUserId) {
                    let headerImg = document.querySelector('#recipient-image img');
                    let headerName = document.getElementById('chat-name');
                    headerImg.src = chat.photoUrl;
                    headerName.innerHTML = chat.chatName;
                    document.getElementById('send-message').dataset.chatid = chat.chatId;
                    getChatMessages(newChatUserId);
                    found = true;
                }
            })
            if (found === false) {
                document.getElementById('chat-bubble-wrapper').innerHTML = ""
                createNewChat(userId, newChatUserId);
            }
        }
    })
    closeOneModal()
}

function createNewChat(userId, newChatUserId) {
    let postParams = {
       method: 'POST', // *GET, POST, PUT, DELETE, etc.
       headers: {
           'Content-Type': 'application/json; charset=UTF-8',
           'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
           'Access-Control-Allow-Origin':'*'
       },
       body: JSON.stringify()
    };
    fetch(`${baseUrl}/${userId}/chats/${newChatUserId}` , postParams)
        .then(res => res.json())
        .then(newChatObj => {
//            console.log(newChatObj);
            createPreviewBoxes(newChatObj);
            let headerImg = document.querySelector('#recipient-image img');
            let headerName = document.getElementById('chat-name');
            let chat = newChatObj.data[newChatObj.data.length-1]
            headerImg.src = chat.photoUrl ? chat.photoUrl : "./images/defaultIcon.svg"
            headerName.innerHTML = chat.chatName;
            document.getElementById('send-message').dataset.chatid = newChatObj.chatId;
    });
}

let deleteChatBtn = document.getElementById('delete-chat-btn');
deleteChatBtn.addEventListener('click', deleteChat);

function deleteChat(event) {
    let chatId = event.target.dataset.chatid
    let postParams = {
       method: 'DELETE', // *GET, POST, PUT, DELETE, etc.
       headers: {
           'Content-Type': 'application/json; charset=UTF-8',
           'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
           'Access-Control-Allow-Origin':'*'
       },
//       body: JSON.stringify()
    };
    fetch(`/muttsapp/message/chat/${chatId}`, postParams)
//        .then(res => res.json())
        .then(res => {
            $('#deleteChatModal').modal('hide');
            location.replace("/muttsapp/index")
    });
    }

function closeOneModal(exampleModal) {
    // get modal
    const modal = document.getElementById('exampleModal');
    // change state like in hidden modal
    modal.classList.remove('show');
    modal.setAttribute('aria-hidden', 'true');
    modal.setAttribute('style', 'display: none');
     // get modal backdrop
     const modalBackdrops = document.getElementsByClassName('modal-backdrop');
     // remove opened modal backdrop
      document.body.removeChild(modalBackdrops[0]);
  }

window.addEventListener('DOMContentLoaded', () => {
    const button = document.querySelector('#emoji-btn');
    const picker = new EmojiButton();
picker.on('emoji', emoji => {
    document.querySelector('#new-message').value += emoji;
});
button.addEventListener('click', () => {
    picker.pickerVisible ? picker.hidePicker() : picker.showPicker(button);
    });
});

let imageForm = document.querySelector('#image-form')
imageForm.addEventListener('submit', sendImage)

function sendImage(event) {
    event.preventDefault();
    let file = new FormData(event.target);
    let postParams = {
       method: 'POST', // *GET, POST, PUT, DELETE, etc.
       headers: {
//           'Content-Type': 'application/json; charset=UTF-8',
           'Access-Control-Allow-Headers': "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
           'Access-Control-Allow-Origin':'*'
       },
       body: file
    };
    for (var value of file.values()) {
    }
    chatId = document.getElementById('image-form').dataset.chatid;
    fetch(`/muttsapp/message/image/${chatId}/${userId}`, postParams)
//        .then(res => res.json())
        .then(imageObj => {
            getUserChats();
            otherUserId = document.getElementById('image-form').dataset.otheruserid;
            getChatMessages(otherUserId);
    });
    $('#uploadModal').modal('hide');
}