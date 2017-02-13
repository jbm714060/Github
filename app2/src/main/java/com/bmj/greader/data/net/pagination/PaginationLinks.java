package com.bmj.greader.data.net.pagination;

import android.text.TextUtils;
import android.util.Log;

import com.bmj.greader.common.wrapper.AppLog;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/29 0029.
 */
public class PaginationLinks {
    public PaginationLink pageFirst;
    public PaginationLink pageNext;
    public PaginationLink pagePrev;
    public PaginationLink pageLast;

    /**
     * @param links <https://api.github.com/search/repositories?q=android%2Blanguage%3Ajava&sort=stars&order=desc&page=2&per_page=30>; rel="next",
     *              <https://api.github.com/search/repositories?q=android%2Blanguage%3Ajava&sort=stars&order=desc&page=34&per_page=30>; rel="last"
     */
    public PaginationLinks(String links){
        if(TextUtils.isEmpty(links)) return;
        String[] link_split = links.split(",");
        for(String item:link_split){
            PaginationLink link = new PaginationLink(item);
            switch(link.relType){
                case RelType.first:
                    pageFirst = link;
                    break;
                case RelType.next:
                    pageNext = link;
                    break;
                case RelType.prev:
                    pagePrev = link;
                    break;
                case RelType.last:
                    pageLast = link;
                    break;
            }
        }
    }

    public int getNextPage(){
        if(pageNext == null)
            return 0;
        else
            return pageNext.getPage();
    }

    public int getLastPage(){
        if(pageLast == null)
            return 0;
        else
            return pageLast.getPage();
    }

    public int getPrevPage(){
        if(pagePrev == null)
            return 0;
        else
            return pagePrev.getPage();
    }

    public int getFirstPage(){
        if(pageFirst == null)
            return 0;
        else
            return pageFirst.getPage();
    }

    public int getPageSize(){
        if(pageLast != null)
            return pageLast.getPer_page();
        else if(pageFirst != null)
            return pageFirst.getPer_page();
        else
            return 30;
    }


    public class PaginationLink{
        private URI uri;
        private  int relType;
        private int page;
        private int per_page = 0;

        public void setUri(String uri){this.uri = URI.create(uri);}
        public URI getUri(){return uri;}
        public void setRelType(@RelType.Type int type){relType = type;}
        public @RelType.Type int getRelType(){return relType;}
        public void setPage(int page){this.page = page;}
        public int getPage(){return page;}
        public void setPer_page(int pageSize){per_page = pageSize;}
        public int getPer_page(){return per_page;}

        public PaginationLink(){}

        public String toLink(){
            StringBuilder builder= new StringBuilder();
            builder.append("<");
            builder.append(uri.toString());
            builder.append(">; rel=\"");
            switch (relType){
                case RelType.first:
                    builder.append("first\"");
                    break;
                case RelType.next:
                    builder.append("next\"");
                    break;
                case RelType.prev:
                    builder.append("prev\"");
                    break;
                case RelType.last:
                    builder.append("last\"");
                    break;
            }
            return builder.toString();
        }

        /**
         * link:<https://api.github.com/search/repositories?q=android%2Blanguage%3Ajava&sort=stars&order=desc&page=2&per_page=30>; rel="next"
         */
        public PaginationLink(String link){
            link = link.replace("<","").replace(">","").replace("\"","").trim();
            String uri = link.split(";")[0];
            String rel = link.split("rel=")[1];

            this.uri = URI.create(uri);
            try{
                if(rel.equals("first"))
                    relType = RelType.first;
                else if(rel.equals("next"))
                    relType = RelType.next;
                else if(rel.equals("prev"))
                    relType = RelType.prev;
                else if(rel.equals("last"))
                    relType = RelType.last;

                String page = splitQuery(this.uri).get("page");
                if(!TextUtils.isEmpty(page))
                    this.page = Integer.valueOf(page);
                String perPage = splitQuery(this.uri).get("per_page");
                if(!TextUtils.isEmpty(perPage))
                    this.per_page = Integer.valueOf(perPage);
                else
                    this.per_page = 30;
            }catch (UnsupportedEncodingException e){
                AppLog.e(e);
                this.relType = -1;
            }
        }
    }

    private Map<String,String> splitQuery(URI uri) throws UnsupportedEncodingException{
        String uriQuery = uri.getQuery();
        Map<String,String> query_pairs = new LinkedHashMap<>();
        String[] subs = uriQuery.split("&");
        for(String item:subs){
            int index = item.indexOf("=");
            query_pairs.put(URLDecoder.decode(item.substring(0,index),"UTF-8"),
                    URLDecoder.decode(item.substring(index+1),"UTF-8"));
        }
        return query_pairs;
    }
}
