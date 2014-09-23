function startIntro() {
    var intro = introJs();
    intro.setOptions({
        steps: [
            {
                element: document.querySelector('#reviewBox'),
                intro: "This is the content of two similar reviews. The red lines are the deleted ones by a given temporal order.",
                position: 'top'
            },
            {
                element: document.querySelector('#dates'),
                intro: "This tile shows how many days elapsed between two similar reviews. " +
                    "When more than a day has elapsed the system marks the older one as the A review, the B review is then the suspicious one",
                position: 'left'
            },
            {
                element: document.querySelector('#titles'),
                intro: 'This tile shows the title by the different users.',
                position: 'left'
            },
            {
                element: document.querySelector('#ratings'),
                intro: "This tile shows the ratings by the different users.",
                position: 'left'
            },
            {
                element: document.querySelector('#products'),
                intro: "This tile shows the products rated by the different users. The names are links to the actual product.",
                position: 'left'
            },
            {
                element: document.querySelector('#authors-intro'),
                intro: "This tile shows the names of the involved authors. " +
                    "The name is a link to further details. " +
                    "The bars indicate on the numbers of reviews a user have. " +
                    "The more bars, the more reviews. " +
                    "The H indicates the H-Score. " +
                    "Green is low, yellow is mid and red is high.",
                position: 'left'
            },
            {
                element: document.querySelector('#occurrences'),
                intro: "This field show how often the review occurs within the set of possible content similarities.",
                position: 'left'
            },
            {
                element: document.querySelector('#vote-intro'),
                intro: "Once you have reviewed the possible information, you can chose on the nature of the review.",
                position: 'left'
            }
        ]
    });

    intro.start();
}