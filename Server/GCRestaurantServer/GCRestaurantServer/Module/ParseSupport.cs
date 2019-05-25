using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using Newtonsoft.Json.Linq;
using HtmlAgilityPack;
using System.Net;
using System.IO;
namespace GCRestaurantServer
{
    class ParseSupport
    {
        private static Regex url_reg = new Regex(@"([0-9a-zA-Z_]+)=([0-9a-zA-Z_]+)", RegexOptions.Compiled);
        private static Regex reg_CyberCampus = new Regex(@"([가-힣0-9a-zA-z\- ]+) \(([0-9]+)_([0-9]+)\)", RegexOptions.Compiled);




        private static Regex utf8_char = new Regex(@"(\\u00([0-9a-fA-F])([0-9a-fA-F]))", RegexOptions.Compiled);
        public static JObject UrlQueryParser(string url)
        {
            MatchCollection gas = url_reg.Matches(url);
            JObject result = new JObject();
            foreach (Match match in gas)
            {
                result[match.Groups[1].Value] = match.Groups[2].Value;
            }
            return result;
        }

        public static string UTF8Char(string data)
        {
            MatchCollection gas = utf8_char.Matches(data);

            List<string> temp = new List<string>();
            foreach (Match match in gas)
            {
                string a1 = match.Groups[2].Value + match.Groups[3].Value;
                if (!temp.Contains(a1)) temp.Add(a1);
            }
            foreach (string chardata in temp)
            {

                char change = (char)Convert.ToInt32(chardata, 16);

                data = data.Replace("\\u00" + chardata, change.ToString());
            }
            return data;
        }
        public static JObject UrlQueryParser(HtmlNode node)
        {
            return UrlQueryParser(node.Attributes["href"].Value);
        }
        public static HtmlDocument Crawling(string url, int retry = 3)
        {
            HttpWebRequest hreq = (HttpWebRequest)WebRequest.Create(url);
            hreq.Method = "GET";
            hreq.ContentType = "application/x-www-form-urlencoded";
            HttpWebResponse hres = null;
            Stream dataStream = null;
            StreamReader sr = null;
            for (int i = 0; i < retry; i++)
            {
                try
                {
                    hres = (HttpWebResponse)hreq.GetResponse();
                    if (hres.StatusCode == HttpStatusCode.OK)
                    {
                        dataStream = hres.GetResponseStream();
                        sr = new StreamReader(dataStream, Encoding.UTF8);
                        HtmlDocument dom = new HtmlDocument();
                        dom.LoadHtml(sr.ReadToEnd());
                        return dom;
                    }
                }
                catch (Exception e)
                {
                    // return null;
                }
                finally
                {
                    if (hres != null) hres.Close();
                    if (dataStream != null) dataStream.Close();
                    if (sr != null) sr.Close();
                }
            }
            return null;
        }

    }
}
