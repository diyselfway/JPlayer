package com.julian.qplayer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Julian on 2016/3/12.
 */
public class ArtistInfo {

    /**
     * name : 朴树
     * mbid : b9a95482-d121-42c5-8a49-b735046e19cc
     * url : http://www.last.fm/music/%E6%9C%B4%E6%A0%91
     * image : [{"#text":"http://img2-ak.lst.fm/i/u/34s/66b3186934b9408a9a166914c5c9521d.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/66b3186934b9408a9a166914c5c9521d.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/66b3186934b9408a9a166914c5c9521d.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/66b3186934b9408a9a166914c5c9521d.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/66b3186934b9408a9a166914c5c9521d.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/66b3186934b9408a9a166914c5c9521d.png","size":""}]
     * streamable : 0
     * ontour : 0
     * stats : {"listeners":"16448","playcount":"184634"}
     * similar : {"artist":[{"name":"许巍","url":"http://www.last.fm/music/%E8%AE%B8%E5%B7%8D","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/badf2643b4744cd2946db31a8eb5f5dc.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/badf2643b4744cd2946db31a8eb5f5dc.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/badf2643b4744cd2946db31a8eb5f5dc.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/badf2643b4744cd2946db31a8eb5f5dc.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/badf2643b4744cd2946db31a8eb5f5dc.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/badf2643b4744cd2946db31a8eb5f5dc.png","size":""}]},{"name":"李志","url":"http://www.last.fm/music/%E6%9D%8E%E5%BF%97","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/c5e3f8b601984e54bbc15cd030fa980f.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/c5e3f8b601984e54bbc15cd030fa980f.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/c5e3f8b601984e54bbc15cd030fa980f.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/c5e3f8b601984e54bbc15cd030fa980f.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/c5e3f8b601984e54bbc15cd030fa980f.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/c5e3f8b601984e54bbc15cd030fa980f.png","size":""}]},{"name":"张悬","url":"http://www.last.fm/music/%E5%BC%A0%E6%82%AC","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/b1e47289a8b94c0d825a3705ef254c21.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/b1e47289a8b94c0d825a3705ef254c21.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/b1e47289a8b94c0d825a3705ef254c21.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/b1e47289a8b94c0d825a3705ef254c21.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/b1e47289a8b94c0d825a3705ef254c21.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/b1e47289a8b94c0d825a3705ef254c21.png","size":""}]},{"name":"宋冬野","url":"http://www.last.fm/music/%E5%AE%8B%E5%86%AC%E9%87%8E","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/cf67a2b750854303c3f78618d8250d8c.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/cf67a2b750854303c3f78618d8250d8c.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/cf67a2b750854303c3f78618d8250d8c.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/cf67a2b750854303c3f78618d8250d8c.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/cf67a2b750854303c3f78618d8250d8c.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/cf67a2b750854303c3f78618d8250d8c.png","size":""}]},{"name":"孫燕姿","url":"http://www.last.fm/music/%E5%AD%AB%E7%87%95%E5%A7%BF","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/8b0ce61589fb463b9c610e0ed9410900.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/8b0ce61589fb463b9c610e0ed9410900.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/8b0ce61589fb463b9c610e0ed9410900.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/8b0ce61589fb463b9c610e0ed9410900.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/8b0ce61589fb463b9c610e0ed9410900.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/8b0ce61589fb463b9c610e0ed9410900.png","size":""}]}]}
     * tags : {"tag":[{"name":"chinese","url":"http://www.last.fm/tag/chinese"},{"name":"folk","url":"http://www.last.fm/tag/folk"},{"name":"pop","url":"http://www.last.fm/tag/pop"},{"name":"chinese school folk","url":"http://www.last.fm/tag/chinese+school+folk"},{"name":"chines pop","url":"http://www.last.fm/tag/chines+pop"}]}
     * bio : {"links":{"link":{"#text":"","rel":"original","href":"http://last.fm/music/%E6%9C%B4%E6%A0%91/+wiki"}},"published":"10 May 2006, 08:26","summary":"朴树 (Pinyin: Pǔ Shù) made his first album debut in 1999 and has gone on to win the hearts of numerous fans in China. He was awarded six trophies (including best male singer, best album, and best producer) at the annual Pepsi Music Chart Awards, which was held in Shenzhen in May of 2004. His song \"Colorful Days\" was also one of the top 10 most popular songs on the Chinese mainland in 2004. He reportedly married Shanghainese TV actress Wu Xiaomin on January 5, 2005. <a href=\"http://www.last.fm/music/%E6%9C%B4%E6%A0%91\">Read more on Last.fm<\/a>","content":"朴树 (Pinyin: Pǔ Shù) made his first album debut in 1999 and has gone on to win the hearts of numerous fans in China. He was awarded six trophies (including best male singer, best album, and best producer) at the annual Pepsi Music Chart Awards, which was held in Shenzhen in May of 2004. His song \"Colorful Days\" was also one of the top 10 most popular songs on the Chinese mainland in 2004. He reportedly married Shanghainese TV actress Wu Xiaomin on January 5, 2005. <a href=\"http://www.last.fm/music/%E6%9C%B4%E6%A0%91\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}
     */

    @SerializedName("artist")
    private ArtistEntity info;

    public void setInfo(ArtistEntity info) {
        this.info = info;
    }

    public ArtistEntity getInfo() {
        return info;
    }

    public static class ArtistEntity {
        /**
         * links : {"link":{"#text":"","rel":"original","href":"http://last.fm/music/%E6%9C%B4%E6%A0%91/+wiki"}}
         * published : 10 May 2006, 08:26
         * summary : 朴树 (Pinyin: Pǔ Shù) made his first album debut in 1999 and has gone on to win the hearts of numerous fans in China. He was awarded six trophies (including best male singer, best album, and best producer) at the annual Pepsi Music Chart Awards, which was held in Shenzhen in May of 2004. His song "Colorful Days" was also one of the top 10 most popular songs on the Chinese mainland in 2004. He reportedly married Shanghainese TV actress Wu Xiaomin on January 5, 2005. <a href="http://www.last.fm/music/%E6%9C%B4%E6%A0%91">Read more on Last.fm</a>
         * content : 朴树 (Pinyin: Pǔ Shù) made his first album debut in 1999 and has gone on to win the hearts of numerous fans in China. He was awarded six trophies (including best male singer, best album, and best producer) at the annual Pepsi Music Chart Awards, which was held in Shenzhen in May of 2004. His song "Colorful Days" was also one of the top 10 most popular songs on the Chinese mainland in 2004. He reportedly married Shanghainese TV actress Wu Xiaomin on January 5, 2005. <a href="http://www.last.fm/music/%E6%9C%B4%E6%A0%91">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.
         */

        private BioEntity bio;
        /**
         * #text : http://img2-ak.lst.fm/i/u/34s/66b3186934b9408a9a166914c5c9521d.png
         * size : small
         */

        private List<ImageEntity> image;

        public void setBio(BioEntity bio) {
            this.bio = bio;
        }

        public void setImage(List<ImageEntity> image) {
            this.image = image;
        }

        public BioEntity getBio() {
            return bio;
        }

        public List<ImageEntity> getImage() {
            return image;
        }

        public static class BioEntity {
            private String summary;

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getSummary() {
                return summary;
            }
        }

        public static class ImageEntity {
            @SerializedName("#text")
            private String url="";
            private String size;

            public void setUrl(String url) {
                this.url = url;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getUrl() {
                return url;
            }

            public String getSize() {
                return size;
            }
        }
    }
}
