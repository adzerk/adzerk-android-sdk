package com.adzerk.android.sdk.rest;


/**
 * Enumeration of Event IDs
 *
 * Events in Adzerk are data about a way a user interacts with an ad. Standard events included with all Adzerk accounts
 * are clicks, impressions, and conversions.
 *
 * Custom events are defined by the user of the account and must be called specifically.
 */
public enum EventId {

    ViewConversion(1),
    ClickConversion(2),
    ServerConversion(3),
    Upvote(10),
    Downvote(11),
    DownvoteUninteresting(12),
    DownvoteMisleading(13),
    DownvoteOffensive(14),
    DownvoteRepetitive(15),
    Like(20),
    Share(21),
    Comment(22),
    Visible(30),
    Hover(31),
    Expand(32),
    ShareFacebook(50),
    ShareTwitter(51),
    SharePinterest(52),
    ShareReddit(53),
    ShareEmail(54),
    Start(70),
    FirstQuartile(71),
    MidPoint(72),
    ThirdQuartile(73),
    Complete(74),
    Mute(75),
    UnMute(76),
    Pause(77),
    Rewind(78),
    Resume(79),
    FullScreen(80),
    ExitFullScreen(81),
    ExpandScreen(82),
    Collapse(83),
    AcceptInvitationLinear(84),
    CloseLinear(85),
    Skip(86),
    Progress(87),
    CommentReply(101),
    CommentUpvote(102),
    CommentDownvote(103),
    Custom01(104),
    Custom02(105),
    Custom03(106),
    Custom04(107),
    Custom05(108),
    Custom06(109),
    Custom07(110);


    private int value;

    EventId(int value) {
        this.value = value;
    }

}
