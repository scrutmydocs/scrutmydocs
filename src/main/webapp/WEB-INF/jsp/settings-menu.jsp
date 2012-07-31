<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<div id="leftMenu" class="span2">
    <%-- Top Bar --%>
    <div class="btn-toolbar">
        <div class="btn-group">
            <button id="btnNewFS" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                <i class="icon-white icon-plus"></i> New River <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li id="btnAddFSRiver"><a href="#">File System River</a></li>
                <li class="disabled"><a href="#">Database River</a></li>
                <li class="disabled"><a href="#">FTP River</a></li>
                <li class="disabled"><a href="#">EMail River</a></li>
                <li class="disabled"><a href="#">RSS River</a></li>
                <li class="disabled"><a href="#">Twitter River</a></li>
                <li id="btnAddDropBoxRiver"><a href="#">Dropbox River</a></li>
            </ul>
        </div>
    </div>
    <%-- Navigation --%>
    <ul class="nav nav-list">
        <li id="rivers-fs" class="nav-header">File System Rivers</li>

        <li id="rivers-db" class="nav-header">Database Rivers</li>
        <%--<li class="disabled"><a class="river-db" href="#"><i class="icon-leaf"></i> My MongoDB</a></li>--%>

        <li class="nav-header">FTP Rivers</li>
        <%--<li class="disabled"><a class="river-ftp" href="#"><i class="icon-download-alt"></i> My FTP</a></li>--%>

        <li class="nav-header">EMail Rivers</li>
        <%--<li class="disabled"><a class="river-email" href="#"><i class="icon-envelope"></i> My Gmail</a></li>--%>

        <li class="nav-header">RSS Rivers</li>
        <%--<li class="disabled"><a class="river-rss" href="#"><i class="icon-signal"></i> My RSS</a></li>--%>

        <li class="nav-header">Twitter Rivers</li>
        <%--<li class="disabled"><a class="river-twitter" href="#"><i class="icon-comment"></i> @ilaborie</a></li>--%>

        <li id="rivers-dropbox" class="nav-header">Dropbox Rivers</li>
    </ul>
</div>