using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Media.Imaging;


namespace DiagramDesigner
{
    public class PointHelper
    {
        public static Point GetPointForConnector(FullyCreatedConnectorInfo connector)
        {
            Point point =new Point();

            switch(connector.Orientation)
            {
                case ConnectorOrientation.TrueOutput:
                    point = new Point(connector.DataItem.Left + (connector.DataItem.ItemWidth / 2), connector.DataItem.Top - (ConnectorInfoBase.ConnectorHeight));
                    break;
                case ConnectorOrientation.FalseOutput:
                    point = new Point(connector.DataItem.Left + (connector.DataItem.ItemWidth / 2), (connector.DataItem.Top + connector.DataItem.ItemHeight) + (ConnectorInfoBase.ConnectorHeight / 2));
                    break;
                case ConnectorOrientation.Output:
                    point = new Point(connector.DataItem.Left + connector.DataItem.ItemWidth + (ConnectorInfoBase.ConnectorWidth), connector.DataItem.Top + (connector.DataItem.ItemHeight / 2));
                    break;
                case ConnectorOrientation.Input:
                    point = new Point(connector.DataItem.Left - ConnectorInfoBase.ConnectorWidth, connector.DataItem.Top + (connector.DataItem.ItemHeight / 2));
                    break;
            }

            return point;
        }


    }
}
